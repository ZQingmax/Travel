param(
    [string]$Endpoint,
    [string]$Region,
    [string]$BucketName,
    [string]$AccessKeyId,
    [string]$AccessKeySecret,
    [string]$SourceDir,
    [switch]$DryRun,
    [switch]$Overwrite,
    [switch]$SkipBuild
)

$ErrorActionPreference = 'Stop'

function Set-EnvIfValue($Name, $Value) {
    if (-not [string]::IsNullOrWhiteSpace($Value)) {
        [Environment]::SetEnvironmentVariable($Name, $Value, 'Process')
    }
}

function Read-RequiredEnv($Name, $Prompt, [switch]$Secret) {
    $current = [Environment]::GetEnvironmentVariable($Name, 'Process')
    if (-not [string]::IsNullOrWhiteSpace($current)) {
        return
    }

    $current = [Environment]::GetEnvironmentVariable($Name, 'User')
    if (-not [string]::IsNullOrWhiteSpace($current)) {
        [Environment]::SetEnvironmentVariable($Name, $current, 'Process')
        return
    }

    if ($Secret) {
        $secure = Read-Host -Prompt $Prompt -AsSecureString
        $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secure)
        try {
            $plain = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
        } finally {
            [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
        }
        [Environment]::SetEnvironmentVariable($Name, $plain, 'Process')
        return
    }

    $value = Read-Host -Prompt $Prompt
    if ([string]::IsNullOrWhiteSpace($value)) {
        throw "Missing required value: $Name"
    }
    [Environment]::SetEnvironmentVariable($Name, $value, 'Process')
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Resolve-Path (Join-Path $scriptDir '..\..')
$springDir = Join-Path $repoRoot 'springboot'
$jarPath = Join-Path $springDir 'target\springboot-0.0.1-SNAPSHOT.jar'

Set-EnvIfValue 'OSS_ENDPOINT' $Endpoint
Set-EnvIfValue 'OSS_REGION' $Region
Set-EnvIfValue 'OSS_BUCKET_NAME' $BucketName
Set-EnvIfValue 'OSS_ACCESS_KEY_ID' $AccessKeyId
Set-EnvIfValue 'OSS_ACCESS_KEY_SECRET' $AccessKeySecret
Set-EnvIfValue 'OSS_MIGRATION_SOURCE_DIR' $SourceDir

[Environment]::SetEnvironmentVariable('OSS_MIGRATION_DRY_RUN', ($(if ($DryRun) { 'true' } else { 'false' })), 'Process')
[Environment]::SetEnvironmentVariable('OSS_MIGRATION_OVERWRITE', ($(if ($Overwrite) { 'true' } else { 'false' })), 'Process')

Push-Location $springDir
try {
    if (-not $SkipBuild) {
        mvn.cmd -q package -DskipTests
    }

    if (-not (Test-Path $jarPath)) {
        throw "Jar not found: $jarPath"
    }

    if (-not $DryRun) {
        Read-RequiredEnv 'OSS_ENDPOINT' 'OSS endpoint, for example https://oss-cn-hangzhou.aliyuncs.com'
        Read-RequiredEnv 'OSS_REGION' 'OSS region, for example cn-hangzhou'
        Read-RequiredEnv 'OSS_BUCKET_NAME' 'OSS bucket name'
        Read-RequiredEnv 'OSS_ACCESS_KEY_ID' 'OSS AccessKeyId'
        Read-RequiredEnv 'OSS_ACCESS_KEY_SECRET' 'OSS AccessKeySecret' -Secret
    }

    java "-Dloader.main=com.mingde.tools.LocalImagesToOssMigrator" -cp $jarPath org.springframework.boot.loader.launch.PropertiesLauncher
} finally {
    Pop-Location
}
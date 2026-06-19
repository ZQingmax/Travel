const baseUrl = (import.meta.env.VITE_BASE_URL || '').replace(/\/$/, '')
const downloadMarker = '/files/download/'

const stripQueryAndHash = (value) => value.split(/[?#]/)[0]

const encodeObjectPath = (value) => value
  .split('/')
  .filter(segment => segment)
  .map(segment => encodeURIComponent(segment))
  .join('/')

export const getFileKey = (value) => {
  if (!value) return ''
  const raw = String(value).trim()
  const markerIndex = raw.indexOf(downloadMarker)
  if (markerIndex >= 0) {
    const objectPath = stripQueryAndHash(raw.substring(markerIndex + downloadMarker.length))
    try {
      return decodeURIComponent(objectPath)
    } catch (error) {
      return objectPath
    }
  }
  return raw
}

export const toFileUrl = (value) => {
  const key = getFileKey(value)
  if (!key) return ''
  if (/^(https?:)?\/\//i.test(key) || key.startsWith('data:') || key.startsWith('blob:')) {
    return key
  }
  if (key.startsWith('/files/download/') || key.startsWith('/api/files/download/')) {
    return key
  }
  return `${baseUrl}/files/download/${encodeObjectPath(key)}`
}

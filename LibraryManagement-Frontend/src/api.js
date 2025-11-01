
const isDev = typeof window !== 'undefined' && window.location.port === '5173'
export const BASE = isDev ? '/api' : (import.meta.env.VITE_API_BASE || '/api')

async function request(path, options = {}) {
  const res = await fetch(`${BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    credentials: 'include',
    ...options,
  })
  const text = await res.text()
  try {
    return { ok: res.ok, status: res.status, data: JSON.parse(text) }
  } catch {
    return { ok: res.ok, status: res.status, data: text }
  }
}

export const api = {
  getBooks: () => request('/books'),
  getBook: (id) => request(`/books/${id}`),
  searchBooks: (params) => {
    // const q = new URLSearchParams(params)
    const q = new URLSearchParams()
    Object.entries(params).forEach(([k, v]) => {
      const val = (v ?? '').toString().trim()
      if (val) q.append(k, val)     // 只追加非空
    })
    return request(`/search?${q.toString()}`)
  },
  register: (payload) =>
    request('/register', { method: 'POST', body: JSON.stringify(payload) }),
  login: (payload) =>
    request('/login', { method: 'POST', body: JSON.stringify(payload) }),
  borrow: (memberId, bookId) =>
    request(`/borrow/${memberId}/${bookId}`, { method: 'POST' }),
  returnBook: (memberId, bookId) =>
    request(`/return/${memberId}/${bookId}`, { method: 'POST' }),
  history: (memberId) => request(`/history/${memberId}`),
}

import React from 'react'
import { api } from './api'

export default function Books({ session }) {
  const [books, setBooks] = React.useState([])
  const [loading, setLoading] = React.useState(true)
  const [q, setQ] = React.useState({ title:'', author:'', genre:'' })
  const [message, setMessage] = React.useState(null)

  async function loadAll() {
    setLoading(true)
    const res = await api.getBooks()
    setLoading(false)
    if (res.ok) setBooks(res.data)
  }

  React.useEffect(() => { loadAll() }, [])

  async function search(e) {
    e.preventDefault()
    setLoading(true)
    const res = await api.searchBooks(q)
    setLoading(false)
    if (res.ok) setBooks(res.data.searchResults || [])
  }

  async function borrow(bookId) {
    if (!session) { setMessage('Please login first.'); return }
    const res = await api.borrow(session.memberId, bookId)
    if (res.ok && !res.data?.error) {
      setMessage(res.data.message + ' Due: ' + res.data.dueDate)
      loadAll()
    } else {
      setMessage(res.data?.error || 'Cannot borrow book')
    }
  }

  async function returnBook(bookId) {
    if (!session) { setMessage('Please login first.'); return }
    const res = await api.returnBook(session.memberId, bookId)
    if (res.ok && !res.data?.error) {
      setMessage(res.data.message)
      loadAll()
    } else {
      setMessage(res.data?.error || 'Cannot return book')
    }
  }

  return (
    <div>
      <form onSubmit={search} style={{display:'grid', gridTemplateColumns:'1fr 1fr 1fr auto', gap:8, marginBottom:12}}>
        <input placeholder="Title" value={q.title} onChange={e=>setQ({...q, title:e.target.value})} />
        <input placeholder="Author" value={q.author} onChange={e=>setQ({...q, author:e.target.value})} />
        <input placeholder="Genre" value={q.genre} onChange={e=>setQ({...q, genre:e.target.value})} />
        <button>Search</button>
      </form>

      {message && <p>{message}</p>}
      {loading ? <p>Loading...</p> : (
        <table border="1" cellPadding="6" style={{width:'100%'}}>
          <thead>
            <tr><th>ID</th><th>Title</th><th>Author</th><th>Genre</th><th>Available</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {books.map(b => (
              <tr key={b.id}>
                <td>{b.id}</td>
                <td>{b.title}</td>
                <td>{b.author}</td>
                <td>{b.genre}</td>
                <td>{b.availableCopies}</td>
                <td style={{display:'flex', gap:8}}>
                  <button onClick={()=>borrow(b.id)}>Borrow</button>
                  <button onClick={()=>returnBook(b.id)}>Return</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

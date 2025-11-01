import React from 'react'
import { Routes, Route, Link, Navigate, useNavigate } from 'react-router-dom'
import Books from './Books'
import Auth from './Auth'
import History from './History'

function useSession() {
  const [session, setSession] = React.useState(() => {
    const raw = localStorage.getItem('session')
    return raw ? JSON.parse(raw) : null
  })
  const save = (s) => {
    setSession(s)
    if (s) localStorage.setItem('session', JSON.stringify(s))
    else localStorage.removeItem('session')
  }
  return [session, save]
}

export default function App() {
  const [session, setSession] = useSession()
  const navigate = useNavigate()
  const logout = () => { setSession(null); navigate('/') }

  return (
    <div style={{maxWidth: 1000, margin: '24px auto', padding: 16, fontFamily: 'system-ui, Arial'}}>
      <header style={{display:'flex', gap:16, alignItems:'center', marginBottom: 24}}>
        <h2 style={{margin:0}}>📚 Library</h2>
        <nav style={{display:'flex', gap:12}}>
          <Link to="/">Books</Link>
          <Link to="/history">History</Link>
          {!session ? <Link to="/auth">Login / Register</Link>
                    : <button onClick={logout}>Logout ({session.firstName})</button>}
        </nav>
      </header>

      <Routes>
        <Route path="/" element={<Books session={session} />} />
        <Route path="/auth" element={<Auth onSignedIn={setSession} />} />
        <Route path="/history" element={session ? <History session={session} /> : <Navigate to="/auth" />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </div>
  )
}

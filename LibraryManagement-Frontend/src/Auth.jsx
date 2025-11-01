import React from 'react'
import { api } from './api'

export default function Auth({ onSignedIn }) {
  const [tab, setTab] = React.useState('login')
  const [loading, setLoading] = React.useState(false)
  const [msg, setMsg] = React.useState(null)

  async function handleRegister(e) {
    e.preventDefault()
    setLoading(true); setMsg(null)
    const form = new FormData(e.target)
    const payload = {
      firstName: form.get('firstName'),
      lastName: form.get('lastName'),
      email: form.get('email'),
      password: form.get('password')
    }
    const res = await api.register(payload)
    setLoading(false)
    if (res.ok && res.data && res.data.memberId) {
      setMsg('Registered! You can login now.')
      setTab('login')
    } else {
      setMsg(res.data?.error || 'Register failed')
    }
  }

  async function handleLogin(e) {
    e.preventDefault()
    setLoading(true); setMsg(null)
    const form = new FormData(e.target)
    const payload = { email: form.get('email'), password: form.get('password') }
    const res = await api.login(payload)
    setLoading(false)
    if (res.ok && !res.data?.error) {
      // Expecting: { message, memberId, firstName, lastName }
      onSignedIn({
        memberId: res.data.memberId,
        firstName: res.data.firstName,
        lastName: res.data.lastName,
        email: payload.email,
      })
    } else {
      setMsg(res.data?.error || 'Login failed')
    }
  }

  return (
    <div>
      <div style={{display:'flex', gap:12, marginBottom:12}}>
        <button onClick={()=>setTab('login')} disabled={tab==='login'}>Login</button>
        <button onClick={()=>setTab('register')} disabled={tab==='register'}>Register</button>
      </div>
      {tab==='login' ? (
        <form onSubmit={handleLogin} style={{display:'grid', gap:8, maxWidth:360}}>
          <input name="email" placeholder="Email" required />
          <input name="password" type="password" placeholder="Password" required />
          <button type="submit" disabled={loading}>{loading?'Working...':'Login'}</button>
          {msg && <p>{msg}</p>}
        </form>
      ):(
        <form onSubmit={handleRegister} style={{display:'grid', gap:8, maxWidth:360}}>
          <input name="firstName" placeholder="First name" required />
          <input name="lastName" placeholder="Last name" required />
          <input name="email" placeholder="Email" required />
          <input name="password" type="password" placeholder="Password" required />
          <button type="submit" disabled={loading}>{loading?'Working...':'Register'}</button>
          {msg && <p>{msg}</p>}
        </form>
      )}
    </div>
  )
}

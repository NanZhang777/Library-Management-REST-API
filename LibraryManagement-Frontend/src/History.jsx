import React from 'react'
import { api } from './api'

export default function History({ session }) {
  const [records, setRecords] = React.useState([])
  React.useEffect(() => {
    api.history(session.memberId).then(res => {
      if (res.ok) setRecords(res.data.borrowingHistory || res.data || [])
    })
  }, [session.memberId])

  return (
    <div>
      <h3>Borrowing History</h3>
      {records.length === 0 ? <p>No records.</p> : (
        <table border="1" cellPadding="6" style={{width:'100%'}}>
          <thead>
            <tr><th>ID</th><th>Book</th><th>Borrowed</th><th>Due</th><th>Returned</th></tr>
          </thead>
          <tbody>
            {records.map(r => (
              <tr key={r.id}>
                <td>{r.id}</td>
                <td>{r.bookTitle} (#{r.bookId})</td>
                <td>{r.borrowDate}</td>
                <td>{r.dueDate}</td>
                <td>{r.returnDate || '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

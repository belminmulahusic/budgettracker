import { useNavigate } from 'react-router-dom'
import { getEmail, logout } from '../services/auth'

export default function DashboardPage() {
  const navigate = useNavigate()
  const email = getEmail()

  function handleLogout() {
    logout()
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-slate-100 px-4 py-10">
      <div className="max-w-4xl mx-auto">
        <div className="bg-white rounded-2xl shadow-lg p-8">
          <div className="flex items-center justify-between gap-4 mb-6">
            <div>
              <h1 className="text-3xl font-bold text-slate-800 mb-2">Dashboard</h1>
              <p className="text-slate-600">
                Logged in as <span className="font-medium">{email}</span>
              </p>
            </div>

            <button
              onClick={handleLogout}
              className="rounded-lg bg-slate-900 text-white px-4 py-2 font-medium hover:bg-slate-800 transition"
            >
              Logout
            </button>
          </div>

          <div className="rounded-xl bg-slate-50 border border-slate-200 p-6">
            <p className="text-slate-700">
              Frontend authentication is working. Transactions UI comes next.
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}
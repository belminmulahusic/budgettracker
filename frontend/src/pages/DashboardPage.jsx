export default function DashboardPage() {
  const email = localStorage.getItem('email')

  return (
    <div className="min-h-screen bg-slate-100 px-4 py-10">
      <div className="max-w-4xl mx-auto">
        <div className="bg-white rounded-2xl shadow-lg p-8">
          <h1 className="text-3xl font-bold text-slate-800 mb-2">Dashboard</h1>
          <p className="text-slate-600 mb-6">
            Logged in as <span className="font-medium">{email}</span>
          </p>

          <div className="rounded-xl bg-slate-50 border border-slate-200 p-6">
            <p className="text-slate-700">
              Frontend authentication is workin.
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}
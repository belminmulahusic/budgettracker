import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import SummaryCard from '../components/SummaryCard'
import TransactionForm from '../components/TransactionForm'
import TransactionTable from '../components/TransactionTable'
import { getEmail, logout } from '../services/auth'
import {
  createTransaction,
  getSummary,
  getTransactions,
} from '../services/transactionService'

export default function DashboardPage() {
  const navigate = useNavigate()
  const email = getEmail()

  const [summary, setSummary] = useState({
    totalIncome: 0,
    totalExpenses: 0,
    balance: 0,
  })

  const [transactions, setTransactions] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  function handleLogout() {
    logout()
    navigate('/login')
  }

  async function loadDashboard() {
    try {
      setLoading(true)
      setError('')

      const [summaryData, transactionData] = await Promise.all([
        getSummary(),
        getTransactions(),
      ])

      setSummary(summaryData)
      setTransactions(transactionData)
    } catch (err) {
      setError(
        err.response?.data?.message ||
        err.message ||
        'Failed to load dashboard'
      )
    } finally {
      setLoading(false)
    }
  }

  async function handleCreateTransaction(data) {
    await createTransaction(data)
    await loadDashboard()
  }

  useEffect(() => {
    loadDashboard()
  }, [])

  function formatCurrency(value) {
    return new Intl.NumberFormat('de-DE', {
      style: 'currency',
      currency: 'EUR',
    }).format(value)
  }

  return (
    <div className="min-h-screen bg-slate-100 px-4 py-10">
      <div className="max-w-5xl mx-auto space-y-6">
        <div className="bg-white rounded-2xl shadow-lg p-8">
          <div className="flex items-center justify-between gap-4 mb-3">
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

          <p className="text-slate-500">
            Overview of your current budget.
          </p>
        </div>

        {loading ? (
          <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-6">
            <p className="text-slate-600">Loading dashboard...</p>
          </div>
        ) : error ? (
          <div className="bg-white rounded-2xl shadow-sm border border-red-200 p-6">
            <p className="text-red-600">{error}</p>
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <SummaryCard
                title="Total Income"
                value={formatCurrency(summary.totalIncome)}
              />
              <SummaryCard
                title="Total Expenses"
                value={formatCurrency(summary.totalExpenses)}
              />
              <SummaryCard
                title="Current Balance"
                value={formatCurrency(summary.balance)}
              />
            </div>

            <TransactionForm onCreate={handleCreateTransaction} />

            <TransactionTable transactions={transactions} />
          </>
        )}
      </div>
    </div>
  )
}
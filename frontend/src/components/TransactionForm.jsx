import { useState } from 'react'

const categories = [
  'FOOD',
  'TRANSPORT',
  'RENT',
  'LEISURE',
  'HEALTH',
  'SHOPPING',
  'SALARY',
  'OTHER',
]

const types = ['INCOME', 'EXPENSE']

function today() {
  return new Date().toISOString().split('T')[0]
}

export default function TransactionForm({ onCreate }) {
  const [formData, setFormData] = useState({
    amount: '',
    type: 'EXPENSE',
    category: 'FOOD',
    date: today(),
    description: '',
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  function handleChange(event) {
    const { name, value } = event.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')
    setLoading(true)

    try {
      await onCreate({
        ...formData,
        amount: Number(formData.amount),
      })

      setFormData({
        amount: '',
        type: 'EXPENSE',
        category: 'FOOD',
        date: today(),
        description: '',
      })
    } catch (err) {
      setError(
        err.response?.data?.message ||
        'Failed to create transaction'
      )
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-6">
      <h2 className="text-xl font-semibold text-slate-800 mb-4">
        Add Transaction
      </h2>

      <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Amount
          </label>
          <input
            name="amount"
            type="number"
            step="0.01"
            min="0.01"
            value={formData.amount}
            onChange={handleChange}
            placeholder="0.00"
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Type
          </label>
          <select
            name="type"
            value={formData.type}
            onChange={handleChange}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
          >
            {types.map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Category
          </label>
          <select
            name="category"
            value={formData.category}
            onChange={handleChange}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
          >
            {categories.map((category) => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Date
          </label>
          <input
            name="date"
            type="date"
            value={formData.date}
            onChange={handleChange}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
            required
          />
        </div>

        <div className="md:col-span-2">
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Description
          </label>
          <input
            name="description"
            type="text"
            value={formData.description}
            onChange={handleChange}
            placeholder="Optional description"
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
          />
        </div>

        {error && (
          <p className="md:col-span-2 text-sm text-red-600">{error}</p>
        )}

        <div className="md:col-span-2">
          <button
            type="submit"
            disabled={loading}
            className="rounded-lg bg-slate-900 text-white px-5 py-2.5 font-medium hover:bg-slate-800 transition disabled:opacity-70"
          >
            {loading ? 'Saving...' : 'Add Transaction'}
          </button>
        </div>
      </form>
    </div>
  )
}
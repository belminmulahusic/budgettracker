const categories = [
  '',
  'FOOD',
  'TRANSPORT',
  'RENT',
  'LEISURE',
  'HEALTH',
  'SHOPPING',
  'SALARY',
  'OTHER',
]

const types = ['', 'INCOME', 'EXPENSE']

export default function TransactionFilters({ filters, onChange, onReset }) {
  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-6">
      <div className="flex items-center justify-between mb-4 gap-4">
        <h2 className="text-xl font-semibold text-slate-800">Filters</h2>

        <button
          onClick={onReset}
          className="text-sm font-medium text-slate-600 hover:text-slate-900"
        >
          Reset
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Type
          </label>
          <select
            name="type"
            value={filters.type}
            onChange={onChange}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
          >
            {types.map((type) => (
              <option key={type} value={type}>
                {type || 'All'}
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
            value={filters.category}
            onChange={onChange}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
          >
            {categories.map((category) => (
              <option key={category} value={category}>
                {category || 'All'}
              </option>
            ))}
          </select>
        </div>

        <div>
        <label className="block text-sm font-medium text-slate-700 mb-1">
            Month
        </label>
        <select
            name="month"
            value={filters.month}
            onChange={onChange}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
        >
            <option value="">All</option>

            {Array.from({ length: 12 }).map((_, index) => {
            const month = String(index + 1).padStart(2, '0')
            const year = new Date().getFullYear()
            const value = `${year}-${month}`

            return (
                <option key={value} value={value}>
                {value}
                </option>
            )
            })}
        </select>
        </div>
      </div>
    </div>
  )
}
function formatCurrency(value) {
  return new Intl.NumberFormat('de-DE', {
    style: 'currency',
    currency: 'EUR',
  }).format(value)
}

function formatDate(value) {
  return new Date(value).toLocaleDateString('de-DE')
}

export default function TransactionTable({ transactions, onDelete }) {
  if (!transactions.length) {
    return (
      <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-6">
        <p className="text-slate-600">No transactions yet.</p>
      </div>
    )
  }

  function handleDelete(id) {
    const confirmed = window.confirm('Delete this transaction?')
    if (confirmed) {
      onDelete(id)
    }
  }

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
      <div className="px-6 py-4 border-b border-slate-200">
        <h2 className="text-xl font-semibold text-slate-800">Transactions</h2>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full min-w-[820px]">
          <thead className="bg-slate-50">
            <tr className="text-left text-sm text-slate-600">
              <th className="px-6 py-3 font-medium">Date</th>
              <th className="px-6 py-3 font-medium">Type</th>
              <th className="px-6 py-3 font-medium">Category</th>
              <th className="px-6 py-3 font-medium">Description</th>
              <th className="px-6 py-3 font-medium text-right">Amount</th>
            </tr>
          </thead>

          <tbody>
            {transactions.map((transaction) => (
              <tr
                key={transaction.id}
                className="border-t border-slate-200 text-sm text-slate-700"
              >
                <td className="px-6 py-4">{formatDate(transaction.date)}</td>
                <td className="px-6 py-4">
                  <span
                    className={`inline-flex rounded-full px-2.5 py-1 text-xs font-medium ${
                      transaction.type === 'INCOME'
                        ? 'bg-green-100 text-green-700'
                        : 'bg-red-100 text-red-700'
                    }`}
                  >
                    {transaction.type}
                  </span>
                </td>
                <td className="px-6 py-4">{transaction.category}</td>
                <td className="px-6 py-4">{transaction.description || '-'}</td>
                <td className="px-6 py-4 text-right font-medium">
                  {formatCurrency(transaction.amount)}
                </td>
                <td className="px-6 py-4 text-right">
                  <button
                    onClick={() => handleDelete(transaction.id)}
                    className="rounded-lg border border-red-200 px-3 py-1.5 text-sm font-medium text-red-600 hover:bg-red-50 transition"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
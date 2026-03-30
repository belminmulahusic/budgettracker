export default function SummaryCard({ title, value }) {
  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-6">
      <p className="text-sm font-medium text-slate-500 mb-2">{title}</p>
      <h2 className="text-3xl font-bold text-slate-800">{value}</h2>
    </div>
  )
}
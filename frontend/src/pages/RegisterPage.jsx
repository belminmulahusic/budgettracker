import { Link } from 'react-router-dom'
import AuthLayout from '../components/AuthLayout'

export default function RegisterPage() {
  return (
    <AuthLayout
      title="Register"
      subtitle="Create your account to start tracking your budget."
    >
      <form className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Email
          </label>
          <input
            type="email"
            placeholder="you@example.com"
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Password
          </label>
          <input
            type="password"
            placeholder="At least 6 characters"
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
          />
        </div>

        <button
          type="submit"
          className="w-full rounded-lg bg-slate-900 text-white py-2.5 font-medium hover:bg-slate-800 transition"
        >
          Register
        </button>
      </form>

      <p className="mt-6 text-sm text-slate-600 text-center">
        Already have an account?{' '}
        <Link to="/login" className="font-medium text-slate-900 hover:underline">
          Login
        </Link>
      </p>
    </AuthLayout>
  )
}
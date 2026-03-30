import { Link } from 'react-router-dom'
import AuthLayout from '../components/AuthLayout'

export default function LoginPage() {
  return (
    <AuthLayout
      title="Login"
      subtitle="Sign in to manage your transactions."
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
            placeholder="••••••••"
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-400"
          />
        </div>

        <button
          type="submit"
          className="w-full rounded-lg bg-slate-900 text-white py-2.5 font-medium hover:bg-slate-800 transition"
        >
          Login
        </button>
      </form>

      <p className="mt-6 text-sm text-slate-600 text-center">
        Don&apos;t have an account?{' '}
        <Link to="/register" className="font-medium text-slate-900 hover:underline">
          Register
        </Link>
      </p>
    </AuthLayout>
  )
}
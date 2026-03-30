export function getToken() {
  return localStorage.getItem('token')
}

export function getEmail() {
  return localStorage.getItem('email')
}

export function isAuthenticated() {
  return !!getToken()
}

export function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('email')
}
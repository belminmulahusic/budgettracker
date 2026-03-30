import api from './api'

export async function getSummary() {
  const response = await api.get('/transactions/summary')
  return response.data
}

export async function getTransactions(params = {}) {
  const response = await api.get('/transactions', { params })
  return response.data
}

export async function createTransaction(data) {
  const response = await api.post('/transactions', data)
  return response.data
}

export async function deleteTransaction(id) {
  await api.delete(`/transactions/${id}`)
}
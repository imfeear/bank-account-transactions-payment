'use client'

import { useState, useEffect } from 'react'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"

export default function PixKeys() {
  const [pixKeys, setPixKeys] = useState<string[]>([]) // Lista de chaves PIX
  const [newPixKey, setNewPixKey] = useState('') // Chave PIX para cadastrar
  const [loading, setLoading] = useState(false) // Estado de carregamento
  const [errorMessage, setErrorMessage] = useState('') // Mensagem de erro
  const [successMessage, setSuccessMessage] = useState('') // Mensagem de sucesso

  // Função para buscar as chaves PIX
  const fetchPixKeys = async () => {
    setLoading(true)
    setErrorMessage('')
    setSuccessMessage('')

    try {
      const token = localStorage.getItem("token") // Token de autenticação

      const response = await fetch(`http://localhost:8081/account/pix-keys`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })

      if (!response.ok) {
        const data = await response.json()
        setErrorMessage(data.message || "Erro ao buscar as chaves PIX.")
        return
      }

      const data = await response.json()
      setPixKeys(data) // Define as chaves no estado
    } catch (error) {
      setErrorMessage("Erro ao conectar com o servidor.")
    } finally {
      setLoading(false)
    }
  }

  // Função para cadastrar uma nova chave PIX
  const registerPixKey = async () => {
    setLoading(true)
    setErrorMessage('')
    setSuccessMessage('')

    try {
      const token = localStorage.getItem("token") // Token de autenticação

      const response = await fetch(`http://localhost:8081/account/register-pix-keys`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ pixKey: newPixKey }), // Envia a nova chave no corpo da requisição
      })

      if (!response.ok) {
        const data = await response.json()
        setErrorMessage(data.message || "Erro ao cadastrar a chave PIX.")
        return
      }

      setSuccessMessage("Chave PIX cadastrada com sucesso!")
      setNewPixKey('') // Limpa o campo de entrada
      fetchPixKeys() // Atualiza a lista de chaves
    } catch (error) {
      setErrorMessage("Erro ao conectar com o servidor.")
    } finally {
      setLoading(false)
    }
  }

  // Buscar as chaves ao carregar o componente
  useEffect(() => {
    fetchPixKeys()
  }, [])

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-50">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6 text-center">Minhas Chaves PIX</h2>

        {/* Mensagens de erro e sucesso */}
        {errorMessage && (
          <p className="text-sm text-red-600 text-center border border-red-300 p-2 rounded bg-red-50">
            {errorMessage}
          </p>
        )}
        {successMessage && (
          <p className="text-sm text-green-600 text-center border border-green-300 p-2 rounded bg-green-50">
            {successMessage}
          </p>
        )}

        {/* Lista de chaves PIX */}
        {loading ? (
          <p className="text-center">Carregando...</p>
        ) : pixKeys.length > 0 ? (
          <ul className="list-disc pl-5 space-y-2">
            {pixKeys.map((key, index) => (
              <li key={index} className="text-gray-700">
                {key}
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-center text-gray-500">Nenhuma chave PIX cadastrada.</p>
        )}

        {/* Formulário para cadastrar uma nova chave PIX */}
        <div className="mt-6">
          <Input
            type="email"
            placeholder="Digite uma nova chave PIX (email)"
            value={newPixKey}
            onChange={(e) => setNewPixKey(e.target.value)}
            className="w-full"
          />
          <Button
            onClick={registerPixKey}
            disabled={!newPixKey || loading}
            className={`mt-4 w-full ${loading ? 'bg-gray-400' : 'bg-blue-600 hover:bg-blue-700'} text-white`}
          >
            {loading ? 'Cadastrando...' : 'Cadastrar Nova Chave'}
          </Button>
        </div>
      </div>
    </div>
  )
}

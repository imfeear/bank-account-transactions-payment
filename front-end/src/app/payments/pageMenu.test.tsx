import React from 'react'
import { render, screen } from '@testing-library/react'
import PageMenu from '@/app/payments/page'

jest.mock('next/link', () => {
  return ({ children, href }: { children: React.ReactNode; href: string }) => (
    <a href={href}>{children}</a>
  )
})

jest.mock('@/components/ui/button', () => ({
  Button: ({ children }: { children: React.ReactNode }) => <button>{children}</button>,
}))

jest.mock('@/components/ui/card', () => ({
  Card: ({ children }: { children: React.ReactNode }) => <div data-testid="card">{children}</div>,
  CardContent: ({ children }: { children: React.ReactNode }) => <div data-testid="card-content">{children}</div>,
  CardHeader: ({ children }: { children: React.ReactNode }) => <div data-testid="card-header">{children}</div>,
  CardTitle: ({ children }: { children: React.ReactNode }) => <h2>{children}</h2>,
}))

describe('PageMenu', () => {
  it('renders the page title', () => {
    render(<PageMenu />)
    expect(screen.getByText('Menu de Pagamentos')).toBeInTheDocument()
  })

  it('renders two cards', () => {
    render(<PageMenu />)
    const cards = screen.getAllByTestId('card')
    expect(cards).toHaveLength(2)
  })

  it('renders the PIX card with correct content', () => {
    render(<PageMenu />)
    expect(screen.getByText('PIX')).toBeInTheDocument()
    expect(screen.getByText('Pague com PIX')).toBeInTheDocument()
    const pixLink = screen.getByRole('link', { name: 'Exibir' })
    expect(pixLink).toHaveAttribute('href', '/payments/payment-pix')
  })

  it('renders the Boleto card with correct content', () => {
    render(<PageMenu />)
    expect(screen.getByText('Pagamento boleto')).toBeInTheDocument()
    expect(screen.getByText('Pague seu boleto')).toBeInTheDocument()
    const boletoLink = screen.getAllByRole('link', { name: 'Exibir' })[1]
    expect(boletoLink).toHaveAttribute('href', '/payments/payment-code')
  })

  it('renders the QrCode and Barcode icons', () => {
    render(<PageMenu />)
    const icons = screen.getAllByTestId('card-content')
    expect(icons).toHaveLength(2)
  
  })
})
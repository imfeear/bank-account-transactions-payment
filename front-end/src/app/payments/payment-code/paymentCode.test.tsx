import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { useRouter } from 'next/navigation';
import AppPaymentPageCode from '@/app/payments/payment-code/AppPaymentPageCode';

jest.mock('next/navigation', () => ({
  useRouter: jest.fn(),
}));

jest.mock("@/components/ui/input", () => ({
  Input: ({ value, onChange, placeholder }: any) => (
    <input
      data-testid="barcode-input"
      value={value}
      onChange={onChange}
      placeholder={placeholder}
    />
  ),
}));

jest.mock("@/components/ui/button", () => ({
  Button: ({ children, onClick }: any) => (
    <button onClick={onClick}>{children}</button>
  ),
}));

jest.mock('lucide-react', () => ({
  Barcode: () => <div data-testid="barcode-icon" />,
}));

describe('AppPaymentPageCode', () => {
  let mockRouter: { push: jest.Mock };

  beforeEach(() => {
    mockRouter = {
      push: jest.fn(),
    };
    (useRouter as jest.Mock).mockReturnValue(mockRouter);
  });

  it('renders the component correctly', () => {
    render(<AppPaymentPageCode />);
    
    expect(screen.getByText('Pagamento de boleto')).toBeInTheDocument();
    expect(screen.getByText('Pagar com código de barras')).toBeInTheDocument();
    expect(screen.getByTestId('barcode-icon')).toBeInTheDocument();
    expect(screen.getByTestId('barcode-input')).toBeInTheDocument();
    expect(screen.getByText('Continuar')).toBeInTheDocument();
  });

  it('updates barcodeInput state when input changes', () => {
    render(<AppPaymentPageCode />);
    
    const input = screen.getByTestId('barcode-input');
    fireEvent.change(input, { target: { value: '12345' } });
    
    expect(input).toHaveValue('12345');
  });

  it('navigates to information page when valid input is provided', () => {
    render(<AppPaymentPageCode />);
    
    const input = screen.getByTestId('barcode-input');
    const continueButton = screen.getByText('Continuar');
    
    fireEvent.change(input, { target: { value: '12345' } });
    fireEvent.click(continueButton);
    
    expect(mockRouter.push).toHaveBeenCalledWith('/payments/payment-code/information-code/');
  });

  it('shows alert when empty input is provided', () => {
    const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});
    render(<AppPaymentPageCode />);
    
    const continueButton = screen.getByText('Continuar');
    fireEvent.click(continueButton);
    
    expect(alertMock).toHaveBeenCalledWith('Por favor, insira um código de barras válido.');
    alertMock.mockRestore();
  });
});
import { ResponsePearson } from "@/app/loans/contract/page";
import { formatCpfOrCnpj } from "./format-cpfOrcnpj";
import { formatStringAdress } from "./formatStringAdress";

export const contractLoan = (
  clientData: ResponsePearson | null,
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  loanData: any,
) => {
  return `CONTRATO DE EMPRÉSTIMO BANCÁRIO

BANCO: JalaBank

CLIENTE:
Nome: ${clientData?.nome ?? ""}
CPF: ${formatCpfOrCnpj(clientData?.CpfCnpj ?? "")}
Endereço: ${formatStringAdress(clientData?.adress)}

Valor do Empréstimo Solicitado: R$ ${loanData.loanAmount.toFixed(2)}
Taxa de Juros: ${loanData.interestRate.toFixed(2)}%
Número de Parcelas: ${loanData.numberOfInstallments}
Valor a Ser Pago: R$ ${loanData.totalToPayBack.toFixed(2)}

Pelo presente instrumento particular de contrato de empréstimo, as partes acima qualificadas, de um lado o JalaBank, e de outro o Cliente, têm entre si justo e contratado o seguinte:

1. OBJETO DO CONTRATO
1.1. O presente contrato tem como objeto a concessão, por parte do JalaBank, de um empréstimo no valor de R$ ${loanData.loanAmount.toFixed(
    2,
  )} ao Cliente, com as condições estabelecidas neste contrato.

2. CONDIÇÕES DE PAGAMENTO
2.1. O Cliente se compromete a pagar o valor do empréstimo em ${
    loanData.numberOfInstallments
  } parcelas mensais no valor de R$ ${(
    loanData.totalToPayBack / loanData.numberOfInstallments
  ).toFixed(
    2,
  )}, com vencimento no dia [dia de vencimento de cada parcela] de cada mês, a partir da data de [data da primeira parcela].
2.2. O valor total a ser pago, incluindo o montante principal e os juros compostos, será de R$ ${loanData.totalToPayBack.toFixed(
    2,
  )}.
2.3. O valor das parcelas inclui juros compostos, aplicados mensalmente sobre o saldo atualizado, que abrange o valor inicial do empréstimo e os juros acumulados.
2.4. O pagamento será realizado por meio de Transferência Bancária.

3. ENCARGOS POR ATRASO
3.1. No caso de atraso no pagamento de qualquer parcela, será aplicada uma multa de [percentual da multa] sobre o valor da parcela em atraso, além de juros de mora de [percentual dos juros] ao mês.

4. RESCISÃO DO CONTRATO
4.1. O JalaBank poderá rescindir o presente contrato de imediato, em caso de inadimplência de três parcelas consecutivas ou qualquer outra violação das cláusulas contratuais, sendo o saldo devedor do empréstimo cobrado de forma integral e imediata.

5. DISPOSIÇÕES GERAIS
5.1. O Cliente declara estar ciente das condições do empréstimo e da responsabilidade de honrar com o pagamento das parcelas conforme o estabelecido neste contrato.
5.2. As partes elegem o foro da comarca de Cochabamba, para dirimir quaisquer controvérsias oriundas do presente contrato.

E por estarem assim justos e contratados, as partes aceitam e assinam o presente instrumento digitalmente, concordando com todos os seus termos e condições.

${Intl.DateTimeFormat("pt-BR", {
  day: "numeric",
  month: "long",
  year: "numeric",
  hour: "numeric",
  minute: "numeric",
}).format(new Date())}

${clientData?.nome}
Assinatura do Cliente

Otaviano
Representante Legal do JalaBank
`;
};

export const formatCpfOrCnpj = (value: string) => {
  if (value.length === 14) {
    return value
      .replace(/\D/g, "")
      .replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, "$1.$2.$3/$4-$5");
  }
  if (value.length === 11) {
    return value
      .replace(/\D/g, "")
      .replace(/(\d{2})(\d{3})(\d{3})(\d{4})/, "$1.$2.$3-$4");
  }

  return value;
};

type Adress = {
  street: string;
  neighborhood: string;
  cep: string;
  number: string;
  state: string;
  city: string;
  complement: string;
};
export const formatStringAdress = (value: Adress | undefined) => {
  if (!value) {
    return "";
  }
  return `${value.street}, ${value.number}, ${value.neighborhood}, ${value.city}, ${value.state}, ${value.cep}, ${value.complement}`;
};

import { redirect } from 'next/navigation';

export default function RootPage() {
    redirect('/login'); // Agora redireciona para a página de login
}


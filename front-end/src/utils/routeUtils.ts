export const routesWithoutSharedLayout = ['/login', '/register', '/forgot-password'];

export function shouldUseSharedLayout(pathname: string): boolean {
    return !routesWithoutSharedLayout.includes(pathname);
}
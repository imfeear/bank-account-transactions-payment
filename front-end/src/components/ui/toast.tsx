// src/components/ui/toast.tsx
import * as React from "react"

export type ToastActionElement = React.ReactNode

export type ToastProps = React.HTMLAttributes<HTMLDivElement> & {
  open?: boolean
  onOpenChange?: (open: boolean) => void
}

export const Toast = React.forwardRef<HTMLDivElement, ToastProps>(
  ({ children, ...props }, ref) => {
    return (
      <div
        ref={ref}
        {...props}
        style={{
          padding: "8px 12px",
          borderRadius: 6,
          border: "1px solid rgba(0,0,0,0.1)",
          background: "white",
          boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
          marginBottom: 8,
          ...(props.style || {}),
        }}
      >
        {children}
      </div>
    )
  }
)
Toast.displayName = "Toast"

export function ToastProvider({ children }: { children: React.ReactNode }) {
  return <>{children}</>
}

export function ToastViewport(
  props: React.HTMLAttributes<HTMLDivElement>
) {
  return (
    <div
      {...props}
      style={{
        position: "fixed",
        bottom: 16,
        right: 16,
        display: "flex",
        flexDirection: "column",
        ...(props.style || {}),
      }}
    />
  )
}

export const ToastTitle = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ children, ...props }, ref) => (
  <div
    ref={ref}
    {...props}
    style={{
      fontWeight: 600,
      marginBottom: 4,
      ...(props.style || {}),
    }}
  >
    {children}
  </div>
))
ToastTitle.displayName = "ToastTitle"

export const ToastDescription = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ children, ...props }, ref) => (
  <div
    ref={ref}
    {...props}
    style={{
      fontSize: 14,
      ...(props.style || {}),
    }}
  >
    {children}
  </div>
))
ToastDescription.displayName = "ToastDescription"

export const ToastClose = React.forwardRef<
  HTMLButtonElement,
  React.ButtonHTMLAttributes<HTMLButtonElement>
>(({ children, ...props }, ref) => (
  <button
    ref={ref}
    {...props}
    style={{
      marginLeft: 8,
      border: "none",
      background: "transparent",
      cursor: "pointer",
      ...(props.style || {}),
    }}
  >
    {children ?? "×"}
  </button>
))
ToastClose.displayName = "ToastClose"

export const ToastAction = React.forwardRef<
  HTMLButtonElement,
  React.ButtonHTMLAttributes<HTMLButtonElement>
>(({ children, ...props }, ref) => (
  <button
    ref={ref}
    {...props}
    style={{
      marginTop: 8,
      padding: "4px 8px",
      borderRadius: 4,
      border: "1px solid rgba(0,0,0,0.1)",
      background: "#f5f5f5",
      cursor: "pointer",
      ...(props.style || {}),
    }}
  >
    {children}
  </button>
))
ToastAction.displayName = "ToastAction"

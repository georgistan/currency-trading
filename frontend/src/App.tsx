import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import WebSocketComponent from './WebSocketComponent'
import { Toaster } from "./components/ui/sonner"

function App() {
  return (
    <>
      <WebSocketComponent />
      <Toaster />
    </>
  )
}

export default App

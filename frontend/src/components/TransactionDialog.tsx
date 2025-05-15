"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "./ui/dialog"
import { Button } from "./ui/button"
import { Input } from "./ui/input"
import { Label } from "./ui/label"
import type { Currency } from "../types/currency"

interface TransactionDialogProps {
  isOpen: boolean
  onClose: () => void
  onConfirm: (amount: number) => void
  currency: Currency | null
  type: "buy" | "sell"
  maxAmount: number
}

export function TransactionDialog({ isOpen, onClose, onConfirm, currency, type, maxAmount }: TransactionDialogProps) {
  const [amount, setAmount] = useState<string>("1")
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (isOpen) {
      setAmount("1")
      setError(null)
    }
  }, [isOpen])

  const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAmount(e.target.value)
    setError(null)
  }

  const handleConfirm = () => {
    const numAmount = Number.parseFloat(amount)

    // if (isNaN(numAmount) || numAmount <= 0) {
    //   setError("Please enter a valid positive amount")
    //   return
    // }

    // if (type === "buy" && numAmount > maxAmount) {
    //   setError("Insufficient funds in your wallet")
    //   return
    // }

    onConfirm(numAmount)
  }

  if (!currency) return null

  return (
    <Dialog open={isOpen} onOpenChange={(open) => !open && onClose()}>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>
            {type === "buy" ? "Buy" : "Sell"} {currency.symbol}
          </DialogTitle>
          <DialogDescription>
            Current price: ${currency.price.toFixed(2)} per {currency.symbol}
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="amount" className="text-right">
              Amount ($)
            </Label>
            <Input
              id="amount"
              type="number"
              value={amount}
              onChange={handleAmountChange}
              className="col-span-3"
              min="0"
              step="0.01"
            />
          </div>
          {error && <p className="text-sm text-red-500">{error}</p>}
          <div className="text-sm text-muted-foreground">
            {type === "buy" ? (
              <p>
                You will receive approximately{" "}
                {amount && !isNaN(Number.parseFloat(amount))
                  ? (Number.parseFloat(amount) / currency.price).toFixed(6)
                  : "0"}{" "}
                {currency.symbol}
              </p>
            ) : (
              <p>
                You will receive ${amount} for your {currency.symbol}
              </p>
            )}
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button onClick={handleConfirm} variant={type === "buy" ? "default" : "destructive"}>
            Confirm {type === "buy" ? "Purchase" : "Sale"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
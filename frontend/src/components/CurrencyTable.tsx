"use client"

import { useState } from "react"
import {
  ArrowDownIcon,
  ArrowUpIcon,
  DollarSignIcon,
  EuroIcon,
  PoundSterlingIcon,
  JapaneseYenIcon as YenIcon,
  BitcoinIcon,
} from "lucide-react"
import { Button } from "./ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "./ui/table"
import { Card, CardContent } from "./ui/card"
import { Badge } from "./ui/badge"
import type { Currency } from "../types/currency"
import { TransactionDialog } from "./TransactionDialog"
import { toast } from "sonner"

export default function CurrencyTable(
    {
        initialCurrencies 
    }:{
        initialCurrencies:Currency[]
    }
) {
  const [wallet, setWallet] = useState(10000)
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [selectedCurrency, setSelectedCurrency] = useState<Currency | null>(null)
  const [transactionType, setTransactionType] = useState<"buy" | "sell">("buy")

  const openBuyDialog = (currency: Currency) => {
    setSelectedCurrency(currency)
    setTransactionType("buy")
    setIsDialogOpen(true)
  }

  const openSellDialog = (currency: Currency) => {
    setSelectedCurrency(currency)
    setTransactionType("sell")
    setIsDialogOpen(true)
  }

  const handleDialogClose = () => {
    setIsDialogOpen(false)
    setSelectedCurrency(null)
  }

  const handleTransactionConfirm = (amount: number) => {
    if (!selectedCurrency) return

    if (transactionType === "buy") {
      if (wallet >= amount) {
        setWallet((prev) => prev - amount)
        toast.success(
          `Bought $${amount.toFixed(2)} worth of ${selectedCurrency.symbol} at $${selectedCurrency.price.toFixed(2)}`,
        )
      } else {
        toast.error("Insufficient funds for this purchase")
      }
    } else {
      setWallet((prev) => prev + amount)
      toast.success(
        `Sold $${amount.toFixed(2)} worth of ${selectedCurrency.symbol} at $${selectedCurrency.price.toFixed(2)}`,
      )
    }

    setIsDialogOpen(false)
    setSelectedCurrency(null)
  }

  return (
    <Card className="w-full">
      <CardContent className="p-6">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold">Currency Exchange Rates</h2>
          <Badge variant="outline" className="px-3 py-1">
            Wallet: ${wallet.toLocaleString()}
          </Badge>
        </div>

        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Currency</TableHead>
                <TableHead>Price (USD)</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {initialCurrencies.map((currency, index) => (
                <TableRow key={index}>
                  <TableCell className="font-medium">
                    <div className="flex items-center gap-2">
                      <span>{currency.symbol}</span>
                    </div>
                  </TableCell>
                  <TableCell>${currency.price.toFixed(2)}</TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-2">
                      <Button
                        size="sm"
                        variant="outline"
                        className="text-green-600 border-green-600 hover:bg-green-50 hover:text-green-700"
                        onClick={() => openBuyDialog(currency)}
                      >
                        Buy
                      </Button>
                      <Button
                        size="sm"
                        variant="outline"
                        className="text-red-600 border-red-600 hover:bg-red-50 hover:text-red-700"
                        onClick={() => openSellDialog(currency)}
                      >
                        Sell
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
        <TransactionDialog
          isOpen={isDialogOpen}
          onClose={handleDialogClose}
          onConfirm={handleTransactionConfirm}
          currency={selectedCurrency}
          type={transactionType}
          maxAmount={wallet}
        />
      </CardContent>
    </Card>
  )
}
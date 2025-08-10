"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Badge } from "@/components/ui/badge"
import {Eye, Trash2, Search, Plus, Minus, X, Edit} from 'lucide-react'
import Loading from "@/components/Loading.jsx";
import {fetchCarts} from "@/http/cartAPI.js";


export default function CartsManager() {
    const [carts, setCarts] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [selectedCart, setSelectedCart] = useState(null)
    const [searchUserId, setSearchUserId] = useState('')

    useEffect(() => {
        fetchCarts().then((res) => {
            setCarts(res.data.content)
            setIsLoading(false)
        })
    }, [])

    const fetchCartByUserId = async () => {

    }

    const handleViewCart = async (cartId) => {

    }

    const handleDeleteCart = async (id) => {

    }

    const handleRemoveItem = async (cartId, itemId) => {

    }

    const handleClearCart = async (cartId) => {

    }

    if (isLoading) {
        return <Loading/>
    }

    return (
        <Card>
            <CardHeader>
                <div className="flex items-center justify-between">
                    <div>
                        <CardTitle>Carts Management</CardTitle>
                        <CardDescription>View and manage user shopping carts</CardDescription>
                    </div>
                    <div className="flex items-center gap-2">
                        <Input
                            placeholder="Search by User ID"
                            value={searchUserId}
                            onChange={(e) => setSearchUserId(e.target.value)}
                            className="w-48"
                        />
                        <Button onClick={fetchCartByUserId} variant="outline">
                            <Search className="h-4 w-4" />
                        </Button>
                        <Button onClick={() => {}} variant="outline">
                            Clear
                        </Button>
                    </div>
                </div>
            </CardHeader>
            <CardContent>
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Cart ID</TableHead>
                            <TableHead>User</TableHead>
                            <TableHead>Items</TableHead>
                            <TableHead>Total</TableHead>
                            <TableHead>Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {carts.map((cart) => (
                            <TableRow key={cart.id}>
                                <TableCell className="font-mono text-sm">{cart.id}</TableCell>
                                <TableCell>
                                    <div>
                                        <div className="font-medium">{cart.user.id}</div>
                                        <div className="text-sm text-muted-foreground">{cart.user.username}</div>
                                        <div className="text-sm text-muted-foreground">{cart.user.email}</div>
                                    </div>
                                </TableCell>
                                <TableCell>
                                    <Badge variant="secondary">{cart.cartItems.length} items</Badge>
                                </TableCell>
                                <TableCell>${cart.totalPrice.toFixed(2)}</TableCell>
                                <TableCell>
                                    <div className="flex items-center gap-2">
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => handleViewCart(cart.id)}
                                        >
                                            <Edit className="h-4 w-4" />
                                        </Button>
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => handleDeleteCart(cart.id)}
                                        >
                                            <Trash2 className="h-4 w-4" />
                                        </Button>
                                    </div>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>

                <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                    <DialogContent className="max-w-4xl">
                        <DialogHeader>
                            <DialogTitle>Cart Details</DialogTitle>
                            <DialogDescription>
                                Manage items in this cart
                            </DialogDescription>
                        </DialogHeader>
                        {selectedCart && (
                            <div className="space-y-4">
                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <Label>Cart ID</Label>
                                        <div className="font-mono text-sm">{selectedCart.id}</div>
                                    </div>
                                    <div>
                                        <Label>User</Label>
                                        <div>{selectedCart.user.username} ({selectedCart.user.email})</div>
                                    </div>
                                </div>

                                <div className="flex items-center justify-between">
                                    <h4 className="font-semibold">Cart Items</h4>
                                    <Button
                                        variant="destructive"
                                        size="sm"
                                        onClick={() => handleClearCart(selectedCart.id)}
                                    >
                                        Clear All Items
                                    </Button>
                                </div>

                                <Table>
                                    <TableHeader>
                                        <TableRow>
                                            <TableHead>Product</TableHead>
                                            <TableHead>Price</TableHead>
                                            <TableHead>Actions</TableHead>
                                        </TableRow>
                                    </TableHeader>
                                    <TableBody>
                                        {selectedCart.cartItems.map((item) => (
                                            <TableRow key={item.product.id}>
                                                <TableCell className="font-medium">{item.product.title}</TableCell>
                                                <TableCell>${item.product.price.toFixed(2)}</TableCell>
                                                <TableCell>
                                                    <Button
                                                        variant="outline"
                                                        size="sm"
                                                        onClick={() => handleRemoveItem(selectedCart.id, item.product.id)}
                                                    >
                                                        <X className="h-4 w-4" />
                                                    </Button>
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>

                                <div className="flex justify-between items-center pt-4 border-t">
                                    <span className="font-semibold">Total: ${selectedCart.totalPrice.toFixed(2)}</span>
                                </div>
                            </div>
                        )}
                    </DialogContent>
                </Dialog>
            </CardContent>
        </Card>
    )
}

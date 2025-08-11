"use client"

import {useState, useEffect, useContext} from "react"
import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table"
import {Dialog, DialogContent, DialogHeader, DialogTitle} from "@/components/ui/dialog"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Badge} from "@/components/ui/badge"
import {Search, Eye} from 'lucide-react'
import Loading from "@/components/Loading.jsx";
import {fetchCart} from "@/http/cartAPI.js";
import {fetchCarts} from "@/http/adminAPI.js"
import {toast} from "sonner";
import {Context} from "@/main.jsx";
import Pages from "@/components/Pages.jsx";
import {observer} from "mobx-react-lite";


function CartsManager() {
    const {product} = useContext(Context)
    const [carts, setCarts] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [selectedCart, setSelectedCart] = useState(null)
    const [searchUserId, setSearchUserId] = useState('')

    useEffect(() => {
        product.setLimit(8)

        fetchCarts(product.currentPage, product.limit).then((res) => {
            setCarts(res.data.content)
            product.setTotalPages(res.data.totalPages)
            setIsLoading(false)
        })
    }, [product.currentPage])

    const handleEditCart = async (cart) => {
        setSelectedCart(cart)
        setIsDialogOpen(true)
    }

    const handleSearch = async () => {
        if (searchUserId.trim() === "") {
            product.setCurrentPage(0)
            fetchCarts(product.currentPage, product.limit).then((res) => {
                setCarts(res.data.content)
                product.setTotalPages(res.data.totalPages)
                setIsLoading(false)
            })
            return
        }

        setIsLoading(true)
        fetchCart(searchUserId, false).then((res) => {
            if (!res) {
                toast.error("No cart with such user found")
                setIsLoading(false)
                return
            }

            setCarts([res.data])
            setIsLoading(false)
        })
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
                        <CardDescription>View user shopping carts</CardDescription>
                    </div>
                    <div className="flex items-center gap-2">
                        <Input
                            placeholder="Search by User ID"
                            value={searchUserId}
                            onChange={(e) => setSearchUserId(e.target.value)}
                            className="w-48"
                        />
                        <Button onClick={() => handleSearch()} variant="outline">
                            <Search className="h-4 w-4"/>
                        </Button>
                    </div>
                </div>
            </CardHeader>
            <CardContent>
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-1/5">Cart ID</TableHead>
                            <TableHead className="w-20">User ID</TableHead>
                            <TableHead className="w-1/16">Items</TableHead>
                            <TableHead>Total</TableHead>
                            <TableHead className="text-right w-1/19">Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {carts.map((cart) => (
                            <TableRow key={cart.id} className="text-gray-300">
                                <TableCell className="font-mono text-sm">{cart.id}</TableCell>
                                <TableCell>
                                        <div className="text-sm">{cart.user.id}</div>
                                </TableCell>
                                <TableCell>
                                    <Badge variant="secondary">{cart.cartItems.length} items</Badge>
                                </TableCell>
                                <TableCell>${cart.totalPrice.toFixed(2)}</TableCell>
                                <TableCell>
                                    <div className="flex justify-end">
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => handleEditCart(cart)}
                                        >
                                            <Eye className="h-4 w-4"/>
                                        </Button>
                                    </div>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>

                <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                    <DialogContent className="!max-w-3xl">
                        <DialogHeader>
                            <DialogTitle className="mb-4">Cart Details</DialogTitle>
                        </DialogHeader>
                        {selectedCart && (
                            <div className="space-y-4">
                                <div className="grid grid-cols-2 gap-4">
                                    <div className="grid gap-y-2">
                                        <Label className="font-semibold">Cart ID</Label>
                                        <div className="font-mono text-muted-foreground">{selectedCart.id}</div>
                                    </div>
                                    <div className="grid gap-y-2">
                                        <Label className="font-semibold">User</Label>
                                        <div
                                            className="text-muted-foreground">{selectedCart.user.username} ({selectedCart.user.email})
                                        </div>
                                    </div>
                                </div>

                                <div className="flex items-center justify-between">
                                    <h4 className="font-semibold">Cart Items</h4>
                                </div>

                                <div className="max-h-64 overflow-y-auto">
                                    <Table>
                                        <TableHeader>
                                            <TableRow>
                                                <TableHead className="w-10">ID</TableHead>
                                                <TableHead className="">Product</TableHead>
                                                <TableHead className="w-1/11">Price</TableHead>
                                            </TableRow>
                                        </TableHeader>
                                        <TableBody>
                                            {selectedCart.cartItems.map((item) => (
                                                <TableRow key={item.product.id} className="text-gray-300">
                                                    <TableCell className="font-medium">{item.product.id}</TableCell>
                                                    <TableCell className="font-medium">{item.product.title}</TableCell>
                                                    <TableCell>${item.product.price.toFixed(2)}</TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </div>

                                <div className="flex justify-between items-center pt-4 border-t">
                                    <span className="font-semibold">Total: ${selectedCart.totalPrice.toFixed(2)}</span>
                                </div>
                            </div>
                        )}
                    </DialogContent>
                </Dialog>
                <Pages/>
            </CardContent>
        </Card>
    )
}

export default observer(CartsManager)

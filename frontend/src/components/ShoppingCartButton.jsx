import {ShoppingCart, Trash2} from 'lucide-react'
import {useState, useMemo, useEffect, useContext} from "react"

import {Button} from "@/components/ui/button"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {ScrollArea} from "@/components/ui/scroll-area"
import {clearCart, deleteCartItem, fetchCart} from "@/http/cartAPI.js";
import {observer} from "mobx-react-lite";
import {Context} from "@/main.jsx";

function ShoppingCartButton() {
    const {user, cart} = useContext(Context)
    const [loading, setLoading] = useState(true)
    const totalItems = cart.cart?.cartItems.length || 0
    const totalPrice = cart.cart?.cartItems?.reduce((sum, item) => sum + item.product.price, 0).toFixed(2) || "0.00";

    useEffect(() => {
        fetchCart(user.user.sub).then(data => {
            cart.setCart(data.data)
            setLoading(false)
        })
    }, [])

    if (loading) {
        return null
    }

    const handleDeleteItem = (id) => {
        deleteCartItem(cart.cart.id, id).then(() => {
            cart.removeCartItem(id)
        })
    }

    const handleClearCart = () => {
        clearCart(cart.cart.id).then(() => {
            cart.clearCart()
        })
    }

    return (
        <DropdownMenu
            modal={false}
        >
            <DropdownMenuTrigger asChild>
                <Button
                    variant="ghost"
                    size="icon"
                    className="relative focus:outline-none focus:ring-0"
                    onFocus={(e) => e.target.blur()}
                >
                    <ShoppingCart className="h-6 w-6"/>
                    {totalItems > 0 && (
                        <span
                            className="absolute -top-1 -right-1 flex h-4 w-4 items-center justify-center rounded-full bg-primary text-xs text-primary-foreground">
              {totalItems}
            </span>
                    )}
                    <span className="sr-only">Shopping Cart</span>
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent className="w-80 p-4 overflow-hidden" align="end">
                <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg">Cart ({totalItems} item{totalItems === 1 ? "" : "s"})</h3>
                    {cart.cart.cartItems.length > 0 && (
                        <Button variant="ghost" size="sm" onClick={handleClearCart}>
                            Clear
                        </Button>
                    )}
                </div>
                {cart.cart.cartItems.length === 0 ? (
                    <p className="text-center text-muted-foreground">Your cart is empty.</p>
                ) : (
                    <>
                        <ScrollArea className="h-48 pr-4">
                            <div className="grid gap-4">
                                {cart.cart.cartItems.map((item) => (
                                    <div key={item.product.id} className="flex items-center gap-3">
                                        <img
                                            src={`${import.meta.env.VITE_API_URL}${item.product.previewImage}`}
                                            alt={item.product.name}
                                            width={50}
                                            height={50}
                                            className="rounded-md object-cover"
                                        />
                                        <div className="flex-1 grid gap-0.5">
                                            <h4 className="font-medium text-sm">{item.product.title}</h4>
                                            <p className="text-muted-foreground text-xs">
                                                ${item.product.price.toFixed(2)}
                                            </p>
                                        </div>
                                        <Button
                                            variant="ghost"
                                            size="icon"
                                            className="h-8 w-8"
                                            onClick={() => handleDeleteItem(item.product.id)}
                                        >
                                            <Trash2 className="h-4 w-4"/>
                                            <span className="sr-only">Remove {item.product.name}</span>
                                        </Button>
                                    </div>
                                ))}
                            </div>
                        </ScrollArea>
                        <DropdownMenuSeparator className="my-4"/>
                        <div className="flex items-center justify-between font-semibold mb-4">
                            <span>Total:</span>
                            <span>${totalPrice}</span>
                        </div>
                        <Button className="w-full">Checkout</Button>
                    </>
                )}
            </DropdownMenuContent>
        </DropdownMenu>
    )
}

export default observer(ShoppingCartButton)

import {ShoppingCart, Trash2} from 'lucide-react'
import {useState, useEffect, useContext} from "react"

import {Button} from "@/components/ui/button"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {ScrollArea} from "@/components/ui/scroll-area"
import {
    checkout,
    clearCart,
    deleteCartItem,
    fetchCart,
    selectAllItems,
    toggleSelectItem,
    unselectAllItems
} from "@/http/cartAPI.js";
import {observer} from "mobx-react-lite";
import {Context} from "@/main.jsx";
import {Checkbox} from "@/components/ui/checkbox.js";
import errorToast from "@/utils/errorToast.jsx";

function ShoppingCartButton() {
    const {user, cart} = useContext(Context)
    const [loading, setLoading] = useState(true)
    const totalItems = cart.cart?.cartItems.length || 0
    const totalPrice = cart.cart?.cartItems?.reduce((sum, item) => sum + item.product.price, 0).toFixed(2) || "0.00";

    const selectedItems = cart.cart?.cartItems?.filter(item => item.selected) || [];
    const selectedCount = selectedItems.length;
    const selectedTotalPrice = selectedItems.reduce((sum, item) => sum + item.product.price, 0).toFixed(2);
    const allSelected = cart.cart?.cartItems?.length > 0 && cart.cart.cartItems.every(item => item.selected);


    useEffect(() => {
        fetchCart(user.user.sub, true).then(data => {
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

    const handleSelected = (item) => {
        toggleSelectItem(cart.cart.id, item.product.id).then(() => {
            item.selected = !item.selected
        }).catch(errorToast)
    }

    const handleSelectAllToggle = () => {
        if (allSelected) {
            unselectAllItems(cart.cart.id)
                .then(() => {
                    cart.cart.cartItems.forEach(item => (item.selected = false));
                })
                .catch(errorToast);
        } else {
            selectAllItems(cart.cart.id)
                .then(() => {
                    cart.cart.cartItems.forEach(item => (item.selected = true));
                })
                .catch(errorToast);
        }
    };

    const handleDeleteSelected = () => {
        Promise.all(selectedItems.map(item => deleteCartItem(cart.cart.id, item.product.id)))
            .then(() => {
                selectedItems.forEach(item => cart.removeCartItem(item.product.id));
            })
            .catch(errorToast);
    };

    const handleCheckout = async () => {
        checkout(
            cart.cart.id,
            "RUB",
            import.meta.env.VITE_URL + "/profile/" + user.user.sub
        )
            .then((res) => {
                window.location.href = res.data.confirmationUrl
            })
            .catch(errorToast)
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
            <DropdownMenuContent
                className="w-80 p-4 mt-1 rounded-t-none border-t-0 bg-background/50 backdrop-blur-lg overflow-hidden"
                align="end">
                <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg">Cart ({totalItems} item{totalItems === 1 ? "" : "s"})</h3>
                </div>
                {cart.cart.cartItems.length === 0 ? (
                    <p className="text-center text-muted-foreground">Your cart is empty.</p>
                ) : (
                    <>
                        <div className="flex justify-between mb-2">
                            <div className="flex items-center gap-2">
                                <Checkbox checked={allSelected} onCheckedChange={handleSelectAllToggle} />
                                <span className="text-sm">Select All</span>
                            </div>

                            <Button
                                variant="ghost"
                                size="sm"
                                disabled={selectedCount === 0}
                                onClick={handleDeleteSelected}
                            >
                                Delete ({selectedCount})
                            </Button>
                        </div>
                        <DropdownMenuSeparator className="my-4"/>
                        <ScrollArea className="h-48 pr-4">
                            <div className="grid gap-4">
                                {cart.cart.cartItems.map((item) => (
                                    <div key={item.product.id} className="flex items-center gap-3">
                                        <Checkbox
                                            checked={item.selected}
                                            onCheckedChange={() => handleSelected(item)}/>
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
                        <div className="flex items-center justify-between text-sm">
                            <span>Selected ({selectedCount} items):</span>
                            <span>${selectedTotalPrice}</span>
                        </div>
                        <div className="flex items-center justify-between font-semibold mb-4">
                            <span>Total:</span>
                            <span>${totalPrice}</span>
                        </div>
                        <Button className="w-full" onClick={handleCheckout}>Checkout</Button>
                    </>
                )}
            </DropdownMenuContent>
        </DropdownMenu>
    )
}

export default observer(ShoppingCartButton)

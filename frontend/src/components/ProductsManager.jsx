import {useContext, useEffect, useState} from "react"
import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Textarea} from "@/components/ui/textarea"
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select"
import {Badge} from "@/components/ui/badge"
import {Plus, Edit, Trash2} from 'lucide-react'
import {observer} from "mobx-react-lite";
import {Context} from "@/main.jsx";
import {fetchCategories, fetchProduct, fetchProducts} from "@/http/productAPI.js";
import SearchBar from "@/components/SearchBar.jsx";
import Pages from "@/components/Pages.jsx";
import {toast} from "sonner";

function ProductsManager() {
    const {product} = useContext(Context);
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [editingProduct, setEditingProduct] = useState(null)

    const [formData, setFormData] = useState({
        name: '',
        description: '',
        price: '',
        categoryId: '',
        imageUrl: '',
        modelUrl: '',
    })

    const [imagePreview, setImagePreview] = useState(null)

    useEffect(() => {
        product.setLimit(8)

        const filters = {
            page: product.currentPage,
            size: product.limit,
        };

        fetchProducts(filters).then((res) => {
            product.setProducts(res.data.content)
            product.setTotalPages(res.data.totalPages);
        })
    }, [product.currentPage])

    const handleSubmit = async (e) => {
        e.preventDefault()
        if (formData.categoryId === '') {
            toast.error("Please select a category")
            return
        }
        setIsDialogOpen(false)
    }

    const handleEdit = (product) => {
        setEditingProduct(product)
        setIsDialogOpen(true)
    }

    const handleDelete = async (id) => {

    }

    const resetForm = () => {
        setEditingProduct(null)
        setImagePreview(null)
    }

    return (
        <Card>
            <CardHeader>
                <div className="flex items-center justify-between">
                    <div>
                        <CardTitle>Products Management</CardTitle>
                        <CardDescription>View, create, edit and delete products</CardDescription>
                    </div>
                    <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                        <DialogTrigger asChild>
                            <Button onClick={resetForm}>
                                <Plus className="h-4 w-4"/>
                                Add Product
                            </Button>
                        </DialogTrigger>
                        <DialogContent className="max-w-2xl max-h-[80vh] overflow-y-auto">
                            <DialogHeader>
                                <DialogTitle>
                                    {editingProduct ? 'Edit Product' : 'Add New Product'}
                                </DialogTitle>
                                <DialogDescription>
                                    {editingProduct ? 'Update product information' : 'Create a new 3D model product'}
                                </DialogDescription>
                            </DialogHeader>
                            <form onSubmit={handleSubmit} className="space-y-6">
                                <div className="grid grid-cols-2 gap-6">
                                    <div className="space-y-2">
                                        <Label htmlFor="name">Product Name</Label>
                                        <Input
                                            id="name"
                                            value={formData.name}
                                            onChange={(e) => setFormData({...formData, name: e.target.value})}
                                            placeholder="Enter product name"
                                            required
                                        />
                                    </div>
                                    <div className="space-y-2">
                                        <Label htmlFor="price">Price ($)</Label>
                                        <Input
                                            id="price"
                                            type="number"
                                            step="0.01"
                                            min="0"
                                            value={formData.price}
                                            onChange={(e) => setFormData({...formData, price: e.target.value})}
                                            placeholder="0.00"
                                            className="[&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none [-moz-appearance:textfield]"
                                            required
                                        />
                                    </div>
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="description">Description</Label>
                                    <Textarea
                                        id="description"
                                        value={formData.description}
                                        onChange={(e) => setFormData({...formData, description: e.target.value})}
                                        placeholder="Describe your 3D model..."
                                        rows={3}
                                        required
                                    />
                                </div>

                                <div className="grid grid-cols-2 gap-6">
                                    <div className="space-y-2">
                                        <Label htmlFor="category">Category</Label>
                                        <Select value={formData.categoryId} onValueChange={(value) => setFormData({
                                            ...formData,
                                            categoryId: value
                                        })} required={true}>
                                            <SelectTrigger className="w-48">
                                                <SelectValue placeholder="Select a category"/>
                                            </SelectTrigger>
                                            <SelectContent className="max-h-[200px] overflow-y-auto">
                                                {product.categories.map((category) => (
                                                    <SelectItem key={category.id} value={String(category.id)}>
                                                        {category.name}
                                                    </SelectItem>
                                                ))}
                                            </SelectContent>
                                        </Select>
                                    </div>
                                    <div className="space-y-2">
                                        <Label htmlFor="ownerId">Owner ID</Label>
                                        <Input
                                            id="ownerId"
                                            value={formData.ownerId}
                                            onChange={(e) => setFormData({...formData, ownerId: e.target.value})}
                                            placeholder="Enter owner ID"
                                            required
                                        />
                                    </div>
                                </div>

                                <div className="space-y-4">
                                    <div className="space-y-2">
                                        <Label htmlFor="imageFile">Preview Image</Label>
                                        <div className="space-y-3">
                                            <Input
                                                id="imageFile"
                                                type="file"
                                                accept="image/*"
                                                onChange={(e) => {
                                                    const file = e.target.files?.[0]
                                                    if (file) {
                                                        // Create preview URL
                                                        const previewUrl = URL.createObjectURL(file)
                                                        setImagePreview(previewUrl)
                                                        // You'll handle the file upload logic
                                                        setFormData({...formData, imageUrl: file.name})
                                                    }
                                                }}
                                                className="mt-1 file:pt-0.5 file:mr-4"
                                                required
                                            />
                                            {imagePreview && (
                                                <div className="flex items-start gap-3">
                                                    <img
                                                        src={imagePreview || "/placeholder.svg"}
                                                        alt="Product preview"
                                                        className="w-32 h-32 object-cover rounded-md border flex-shrink-0"
                                                    />
                                                    <Button
                                                        type="button"
                                                        variant="outline"
                                                        size="sm"
                                                        onClick={() => {
                                                            setImagePreview(null)
                                                            setFormData({...formData, imageUrl: ''})
                                                        }}
                                                    >
                                                        Remove
                                                    </Button>
                                                </div>
                                            )}
                                        </div>
                                    </div>

                                    <div className="space-y-2">
                                        <Label htmlFor="modelFile">3D Model File</Label>
                                        <div className="space-y-3">
                                            <Input
                                                id="modelFile"
                                                type="file"
                                                accept=".stl"
                                                onChange={(e) => {
                                                    const file = e.target.files?.[0]
                                                    if (file) {
                                                        // You'll handle the file upload logic
                                                        setFormData({...formData, modelUrl: file.name})
                                                    }
                                                }}
                                                className="mt-1 file:pt-0.5 file:mr-4"
                                                required
                                            />
                                            {editingProduct?.modelUrl && (
                                                <div className="flex items-center gap-2 p-3 bg-muted rounded-md">
                                                    <div className="flex-1">
                                                        <p className="text-sm font-medium">Current Model</p>
                                                        <p className="text-xs text-muted-foreground">{editingProduct.name}_model</p>
                                                    </div>
                                                    <Button
                                                        type="button"
                                                        variant="outline"
                                                        size="sm"
                                                        onClick={handleDownloadModel}
                                                    >
                                                        Download
                                                    </Button>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </div>

                                <DialogFooter className="gap-2">
                                    <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)}>
                                        Cancel
                                    </Button>
                                    <Button type="submit" className="min-w-[100px]">
                                        {editingProduct ? 'Update' : 'Create'} Product
                                    </Button>
                                </DialogFooter>
                            </form>
                        </DialogContent>
                    </Dialog>
                </div>
            </CardHeader>
            <CardContent>
                <SearchBar/>
                <Table className="table-fixed w-full">
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-10">ID</TableHead>
                            <TableHead className="w-1/5">Name</TableHead>
                            <TableHead className="w-1/5">Category</TableHead>
                            <TableHead className="w-20">Price</TableHead>
                            <TableHead className="w-20">Owner ID</TableHead>
                            <TableHead className="w-28">Created</TableHead>
                            <TableHead className="text-right pr-10 w-28">Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {product.products.map((product) => (
                            <TableRow key={product.id} className="text-gray-300">
                                <TableCell className="truncate">{product.id}</TableCell>
                                <TableCell className="truncate">{product.title}</TableCell>
                                <TableCell className="truncate">{product.category.name}</TableCell>
                                <TableCell>${product.price.toFixed(2)}</TableCell>
                                <TableCell className="truncate">{product.owner.username}</TableCell>
                                <TableCell>{new Date(product.createdAt).toLocaleDateString()}</TableCell>
                                <TableCell>
                                    <div className="flex items-center gap-2 justify-end">
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => handleEdit(product)}
                                        >
                                            <Edit className="h-4 w-4"/>
                                        </Button>
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => handleDelete(product.id)}
                                        >
                                            <Trash2 className="h-4 w-4"/>
                                        </Button>
                                    </div>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <Pages/>
            </CardContent>
        </Card>
    )
}

export default observer(ProductsManager)

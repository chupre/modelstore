import {useState, useEffect, useContext} from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Plus, Edit, Trash2 } from 'lucide-react'
import {observer} from "mobx-react-lite";
import {Context} from "@/main.jsx";
import {toast} from "sonner";
import {createCategory, deleteCategory, fetchCategories, updateCategory} from "@/http/productAPI.js";
import Loading from "@/components/Loading.jsx";
import Pages from "@/components/Pages.jsx";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent, AlertDialogDescription, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger
} from "@/components/ui/alert-dialog.js";

function CategoryManager() {
    const {product} = useContext(Context)
    const [isLoading, setIsLoading] = useState(true)
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [editingCategory, setEditingCategory] = useState(null)

    const [formData, setFormData] = useState({
        name: '',
    })

    useEffect(() => {
        product.setLimit(8)
        fetchCategories(product.currentPage, product.limit).then((res) => {
            product.setCategories(res.data.content)
            product.setTotalPages(res.data.totalPages)
            setIsLoading(false)
        })
    }, [product.currentPage])

    const handleSubmit = async (e) => {
        e.preventDefault()

        try {
            editingCategory ? await updateCategory(editingCategory.id, formData) : await createCategory(formData)
            fetchCategories(product.currentPage, product.limit).then((res) => {
                product.setCategories(res.data.content)
                product.setTotalPages(res.data.totalPages)
                setIsLoading(false)
            })
            toast.success("Success", {
                description: `Category successfully ${editingCategory ? "updated" : "created"}`,
            })
            setIsDialogOpen(false)
        } catch(e) {
            const responseData = e?.response?.data;
            if (responseData && typeof responseData === "object") {
                const messages = Object.values(responseData);

                toast.error('Error', {
                    description: (
                        messages.map((msg, i) => (
                            <p key={i}>{msg}</p>
                        ))
                    ),
                });
            } else {
                toast.error("Unexpected error", {
                    description: "Something went wrong. Please try again.",
                });
            }
        }
    }

    const handleEdit = (category) => {
        setEditingCategory(category)
        setFormData({
            name: category.name,
        })
        setIsDialogOpen(true)
    }

    const handleDelete = async (id) => {
        try {
            await deleteCategory(id)
            fetchCategories(product.currentPage, product.limit).then((res) => {
                product.setCategories(res.data.content)
                product.setTotalPages(res.data.totalPages)
                setIsLoading(false)
            })
            toast.success("Success", {
                description: `Category successfully deleted`,
            })
            setIsDialogOpen(false)
        } catch(e) {
            const responseData = e?.response?.data;
            if (responseData && typeof responseData === "object") {
                const messages = Object.values(responseData);

                toast.error('Error', {
                    description: (
                        messages.map((msg, i) => (
                            <p key={i}>{msg}</p>
                        ))
                    ),
                });
            } else {
                toast.error("Unexpected error", {
                    description: "Something went wrong. Please try again.",
                });
            }
        }
    }

    const resetForm = () => {
        setFormData({
            name: '',
        })
        setEditingCategory(null)
    }

    if (isLoading) {
        return <Loading/>
    }

    return (
        <Card>
            <CardHeader>
                <div className="flex items-center justify-between">
                    <div>
                        <CardTitle>Categories Management</CardTitle>
                        <CardDescription>View, create and edit categories</CardDescription>
                    </div>
                    <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                        <DialogTrigger asChild>
                            <Button onClick={resetForm}>
                                <Plus className="h-4 w-4 mr-2" />
                                Add Category
                            </Button>
                        </DialogTrigger>
                        <DialogContent>
                            <DialogHeader>
                                <DialogTitle>
                                    {editingCategory ? 'Edit Category' : 'Add New Category'}
                                </DialogTitle>
                                <DialogDescription>
                                    {editingCategory ? 'Update category information' : 'Create a new product category'}
                                </DialogDescription>
                            </DialogHeader>
                            <form onSubmit={handleSubmit} className="space-y-4">
                                <div className="space-y-2">
                                    <Label htmlFor="name">Name</Label>
                                    <Input
                                        id="name"
                                        value={formData.name}
                                        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                                        required
                                    />
                                </div>
                                <DialogFooter>
                                    <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)}>
                                        Cancel
                                    </Button>
                                    <Button type="submit">
                                        {editingCategory ? 'Update' : 'Create'} Category
                                    </Button>
                                </DialogFooter>
                            </form>
                        </DialogContent>
                    </Dialog>
                </div>
            </CardHeader>
            <CardContent>
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-10">ID</TableHead>
                            <TableHead className="">Name</TableHead>
                            <TableHead className="text-right pr-10 w-28">Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {product.categories.map((category) => (
                            <TableRow key={category.id} className="text-gray-300">
                                <TableCell>{category.id}</TableCell>
                                <TableCell className="font-medium">{category.name}</TableCell>
                                <TableCell>
                                    <div className="flex items-center gap-2">
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => handleEdit(category)}
                                        >
                                            <Edit className="h-4 w-4" />
                                        </Button>
                                        <AlertDialog>
                                            <AlertDialogTrigger
                                                className="h-8 rounded-md gap-1.5 px-3 border bg-background shadow-xs hover:bg-accent hover:text-accent-foreground dark:bg-input/30 dark:border-input dark:hover:bg-input/50"
                                            >
                                                <Trash2 className="h-4 w-4"/>
                                            </AlertDialogTrigger>
                                            <AlertDialogContent>
                                                <AlertDialogHeader>
                                                    <AlertDialogTitle>Are you sure?</AlertDialogTitle>
                                                    <AlertDialogDescription>
                                                        This action cannot be undone. This will permanently delete this
                                                        category
                                                        and remove your data from our servers.
                                                    </AlertDialogDescription>
                                                </AlertDialogHeader>
                                                <AlertDialogFooter>
                                                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                                                    <AlertDialogAction
                                                        onClick={() => handleDelete(category.id)}>Continue</AlertDialogAction>
                                                </AlertDialogFooter>
                                            </AlertDialogContent>
                                        </AlertDialog>
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


export default observer(CategoryManager)
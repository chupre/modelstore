"use client"

import {useState, useEffect, useContext} from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Badge } from "@/components/ui/badge"
import { Edit, Trash2 } from 'lucide-react'
import Loading from "@/components/Loading.jsx";
import {observer} from "mobx-react-lite";
import {Context} from "@/main.jsx";
import {deleteUser, fetchUsers, updateUser} from "@/http/adminAPI.js";
import Pages from "@/components/Pages.jsx";
import {toast} from "sonner";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent, AlertDialogDescription, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger
} from "@/components/ui/alert-dialog.js";
import errorToast from "@/utils/errorToast.jsx";

function UsersManager() {
    const {product} = useContext(Context)
    const [users, setUsers] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [editingUser, setEditingUser] = useState(null)

    const [totalPages, setTotalPages] = useState(0)
    const [currentPage, setCurrentPage] = useState(0)

    const [formData, setFormData] = useState({
        username: '',
        email: '',
        role: 'USER',
    })

    useEffect(() => {
        product.setLimit(8)

        fetchUsers(currentPage, product.limit).then((res) => {
            setUsers(res.data.content)
            setTotalPages(res.data.totalPages)
            setIsLoading(false)
        })
    }, [currentPage])

    const handleSubmit = async (e) => {
        e.preventDefault()

        try {
            setIsLoading(true)
            await updateUser(editingUser.id, formData)
            fetchUsers(currentPage, product.limit).then((res) => {
                setUsers(res.data.content)
                setTotalPages(res.data.totalPages)
                setIsLoading(false)
                setIsDialogOpen(false)
                toast.success("User successfully updated")
            })
        } catch (e) {
            setIsLoading(false);
            errorToast(e)
        }
    }

    const handleEdit = (user) => {
        setEditingUser(user)
        setFormData({
            username: user.username,
            email: user.email,
            role: user.role,
        })
        setIsDialogOpen(true)
    }

    const handleDelete = async (id) => {
        try {
            setIsLoading(true)
            await deleteUser(id)
            fetchUsers(currentPage, product.limit).then((res) => {
                setUsers(res.data.content)
                setTotalPages(res.data.totalPages)
                setIsLoading(false)
                setIsDialogOpen(false)
                toast.success("User successfully deleted")
            })
        } catch (e) {
            setIsLoading(false);
            errorToast(e)
        }
    }

    if (isLoading) {
        return <Loading/>
    }

    return (
        <Card>
            <CardHeader>
                <div className="flex items-center justify-between">
                    <div>
                        <CardTitle>Users Management</CardTitle>
                        <CardDescription>Manage user accounts and permissions</CardDescription>
                    </div>
                    <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                        <DialogContent>
                            <DialogHeader>
                                <DialogTitle>
                                    Edit User
                                </DialogTitle>
                                <DialogDescription>
                                    Update user information
                                </DialogDescription>
                            </DialogHeader>
                            <form onSubmit={handleSubmit} className="space-y-4">
                                <div className="space-y-2">
                                    <Label htmlFor="name">Username</Label>
                                    <Input
                                        id="name"
                                        value={formData.username}
                                        onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                                        required
                                    />
                                </div>
                                <div className="space-y-2">
                                    <Label htmlFor="email">Email</Label>
                                    <Input
                                        id="email"
                                        type="email"
                                        value={formData.email}
                                        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                                        required
                                    />
                                </div>
                                <div className="grid grid-cols-2 gap-4">
                                    <div className="space-y-2">
                                        <Label htmlFor="role">Role</Label>
                                        <Select value={formData.role} onValueChange={(value) => setFormData({ ...formData, role: value })}>
                                            <SelectTrigger>
                                                <SelectValue />
                                            </SelectTrigger>
                                            <SelectContent>
                                                <SelectItem value="SELLER">Seller</SelectItem>
                                                <SelectItem value="BUYER">Buyer</SelectItem>
                                                <SelectItem value="ADMIN">Admin</SelectItem>
                                            </SelectContent>
                                        </Select>
                                    </div>
                                </div>
                                <DialogFooter>
                                    <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)}>
                                        Cancel
                                    </Button>
                                    <Button type="submit">
                                        Update User
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
                            <TableHead className="w-1/6">Username</TableHead>
                            <TableHead className="w-1/5">Email</TableHead>
                            <TableHead className="w-1/12">Role</TableHead>
                            <TableHead>Created</TableHead>
                            <TableHead className="text-right">Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {users.map((user) => (
                            <TableRow key={user.id} className="text-gray-300">
                                <TableCell>{user.id}</TableCell>
                                <TableCell>{user.username}</TableCell>
                                <TableCell>{user.email}</TableCell>
                                <TableCell>
                                    <Badge variant={user.role === 'ADMIN' ? 'default' : 'secondary'}>
                                        {user.role}
                                    </Badge>
                                </TableCell>
                                <TableCell>{new Date(user.createdAt).toLocaleDateString()}</TableCell>
                                <TableCell className="flex justify-end">
                                    <div className="flex items-center gap-2">
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => handleEdit(user)}
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
                                                        user
                                                        and remove your data from our servers.
                                                    </AlertDialogDescription>
                                                </AlertDialogHeader>
                                                <AlertDialogFooter>
                                                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                                                    <AlertDialogAction
                                                        onClick={() => handleDelete(user.id)}>Continue</AlertDialogAction>
                                                </AlertDialogFooter>
                                            </AlertDialogContent>
                                        </AlertDialog>
                                    </div>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <Pages totalPages={totalPages} setCurrentPage={setCurrentPage} currentPage={currentPage}/>
            </CardContent>
        </Card>
    )
}

export default observer(UsersManager)
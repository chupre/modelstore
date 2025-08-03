import { Card, CardContent, CardFooter } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

export function ProductCard({ title, image, price }) {
  return (
    <Card className="w-full max-w-sm overflow-hidden shadow-md hover:shadow-xl transition-shadow rounded-2xl p-0">
      <img
        src={image}
        alt={title}
        className="w-full h-86 object-cover rounded-t-2xl"
      />
      <CardContent className="px-4 pt-4 pb-2">
        <h3 className="text-lg font-semibold text-left">{title}</h3>
      </CardContent>
      <CardFooter className="flex justify-between items-center px-4 pb-4">
        <span className="text-base font-medium">${price.toFixed(2)}</span>
        <Button size="sm">View</Button>
      </CardFooter>
    </Card>
  );
}

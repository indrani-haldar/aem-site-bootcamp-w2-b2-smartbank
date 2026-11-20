use(function(){
	
	var quantity=resource.properties["cosmeticsQuantity"];
	var brand=resource.properties["cosmeticsBrand"];
	var price=resource.properties["cosmeticsPrice"];
	var dis=resource.properties["discount"];
	
	
	if(quantity>5 && brand=="ponds")
	{
		var totalprice=price-(price*dis/100);
	}
	
	return totalprice;
	
}
)
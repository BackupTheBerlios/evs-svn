package evsbsp.server;

/* Automatically generated by EVS JavaCodeGenerator */

import java.util.HashMap;
import java.lang.Integer;
import evs.core.*;
import evs.interfaces.*;
import evs.exception.*;

public class EcommerceInvoker extends AInvoker{

	static private final HashMap<String, Integer> operations = new HashMap<String, Integer>();

	static{
		operations.put("buyOrder", new java.lang.Integer(0));
		operations.put("createCustomer", new java.lang.Integer(1));
		operations.put("createProduct", new java.lang.Integer(2));
		operations.put("listProducts", new java.lang.Integer(3));
		operations.put("login", new java.lang.Integer(4));
		operations.put("updateCustomer", new java.lang.Integer(5));
		operations.put("updateProduct", new java.lang.Integer(6));
		operations.put("newInstance", new java.lang.Integer(7));
		operations.put("keepAlive", new java.lang.Integer(8));
		operations.put("destroy", new java.lang.Integer(9));
	}

	public IInvocationObject invoke(IInvocationObject object) throws RemotingException{

		for(IInterceptor interceptor: Common.getServerInterceptors().getInterceptors()){
			interceptor.beforeInvocation(object);
		}

		Ecommerce localObject = null;

		Integer index = (Integer) operations.get(object.getOperationName());
		if(index == null) throw new IllegalMethodException("Method " + object.getOperationName() + " not supported");

		if(index < 2){
			localObject = (Ecommerce) Common.getObjectManager().invocationArrived(object.getObjectReference().getReference());
			if(localObject == null) throw new IllegalObjectException("Object " + object.getObjectReference().getReference().getClientDependent() + " not available");
		}

		switch(index.intValue()){
			case 0:
				localObject.buyOrder((evsbsp.entities.Order) object.getArguments().get(0), (IACT) object.getArguments().get(1));
				break;
			case 1:
				localObject.createCustomer((evsbsp.entities.Customer) object.getArguments().get(0), (IACT) object.getArguments().get(1));
				break;
			case 2:
				localObject.createProduct((evsbsp.entities.Product) object.getArguments().get(0), (IACT) object.getArguments().get(1));
				break;
			case 3:
				Object returnValue = localObject.listProducts((IACT) object.getArguments().get(0));
				object.setReturnParam(returnValue);
				break;
			case 4:
				returnValue = localObject.login((String) object.getArguments().get(0), (String) object.getArguments().get(1), (IACT) object.getArguments().get(2));
				object.setReturnParam(returnValue);
				break;
			case 5:
				localObject.updateCustomer((evsbsp.entities.Customer) object.getArguments().get(0), (IACT) object.getArguments().get(1));
				break;
			case 6:
				localObject.updateProduct((evsbsp.entities.Product) object.getArguments().get(0), (IACT) object.getArguments().get(1));
				break;
			case 7:
				Integer id = Common.getObjectManager().newInstance(object.getObjectReference().getReference());
				object.setReturnParam(id.toString());
				return object;
			case 8:
				Common.getObjectManager().keepAlive(object.getObjectReference().getReference());
				return object;
			case 9:
				Common.getObjectManager().destroy(object.getObjectReference().getReference());
				return object;
			default:
				throw new IllegalMethodException("Method " +  object.getOperationName() + " not supported");
		}
		
		Common.getObjectManager().invocationDone(object.getObjectReference().getReference(), localObject);

		for(IInterceptor interceptor: Common.getServerInterceptors().getInterceptors()){
			interceptor.afterInvocation(object);
		}

		return object;
	}
}
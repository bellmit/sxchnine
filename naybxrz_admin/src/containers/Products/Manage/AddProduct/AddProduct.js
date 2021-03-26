import React, {PureComponent} from "react";
import FormProduct from "../Form/FormProduct";

class AddProduct extends PureComponent {

    render() {
        return (
            <FormProduct {...this.props}
                         productByIdData={undefined}
                         editMode={false}/>
        );
    }
}

export default AddProduct;
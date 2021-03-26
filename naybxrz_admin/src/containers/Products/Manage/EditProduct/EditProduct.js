import React, {PureComponent} from "react";
import {connect} from 'react-redux';
import FormProduct from "../Form/FormProduct";

class EditProduct extends PureComponent {

    render() {
        return (
            <FormProduct {...this.props}
                         productByIdData={this.props.productByIdData}
                         editMode={true}
            />
        );
    }
}

const mapStateToProps = state => {
    return {
        productByIdData: state.products.productByIdData,
    }
}


export default connect(mapStateToProps)(EditProduct);
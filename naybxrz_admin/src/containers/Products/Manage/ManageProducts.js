import React, {Component} from 'react';
import Aux from '../../../adhoc/Aux/Aux';
import SearchProducts from "./SearchProducts/SearchProducts";
import GridProducts from "./GridProducts/GridProducts";

class ManageProducts extends Component {

    render() {

        return (
            <Aux>
                <SearchProducts />
                <GridProducts {...this.props} />
            </Aux>
        );
    }
}

export default ManageProducts;
import React, {Component} from 'react';
import Aux from '../../../adhoc/Aux/Aux';
import SearchOrders from './SearchOrders/SearchOrders';
import GridOrders from "./GridOrders/GridOrders";

class ManageOrders extends Component {

    render() {
        return (
            <Aux>
                <SearchOrders />
                <GridOrders {...this.props}/>
            </Aux>
        );
    }
}

export default ManageOrders;
import React, {Component} from 'react';
import Aux from '../../../adhoc/Aux/Aux';
import SearchUsers from "../SearchUsers/SearchUsers";
import GridUsers from "../GridUsers/GridUsers";


class ManageUsers extends Component {

    render() {

        return (
            <Aux>
                <SearchUsers/>
                <GridUsers {...this.props} />
            </Aux>
        );
    }
}

export default ManageUsers;
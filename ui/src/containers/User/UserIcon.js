import React, {Component} from 'react';
import Aux from '../../hoc/Aux/Aux';
import './User.css';
import orders_box from './orders_box.png';

class UserIcon extends Component {

    render() {
        let userButton = null;

        if (this.props.user.email != null){
            userButton = <img src={orders_box}
                              className="User-button-icon"
                              alt="Orders History Naybxrz"
                              onClick={this.props.show}/>
        }

        return (
            <Aux>
                {userButton}
            </Aux>
        );
    }
}


export default UserIcon;

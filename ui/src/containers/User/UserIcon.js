import React, {Component} from 'react';
import { Button, Icon } from 'semantic-ui-react';
import Aux from '../../hoc/Aux/Aux';
import './User.css';

class UserIcon extends Component {

    render() {
        let userButton = null;

        if (this.props.user.email != null){
            userButton = <Button circular
                                 color='black'
                                 className="User-button-icon"
                                 style={{top: this.props.top}}
                                 onClick={this.props.show}>
                            <Icon name="child"
                                  size='large'
                                  style={{top: this.props.topIcon}}
                                  className="User-div-button-icon" />
                        </Button>
        }

        return (
            <Aux>
                {userButton}
            </Aux>
        );
    }
}


export default UserIcon;

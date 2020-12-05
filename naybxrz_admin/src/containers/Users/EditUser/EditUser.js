import React, {PureComponent} from "react";
import {connect} from 'react-redux';
import FormUser from "../Form/FormUser";

class EditUser extends PureComponent {

    render() {

        return (
            <FormUser {...this.props}
                      getUserData={this.props.getUserData}
                      editMode={true}/>
        );
    }
}

const mapStateToProps = state => {
    return {
        getUserData: state.user.getUserData,
    }
}


export default connect(mapStateToProps)(EditUser);
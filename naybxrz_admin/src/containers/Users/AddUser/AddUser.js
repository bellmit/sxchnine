import React, {PureComponent} from "react";
import FormUser from "../Form/FormUser";

class AddUser extends PureComponent {

    render() {

        return (
            <FormUser {...this.props}
                      getUserData={undefined}
                      editMode={false}/>
        );
    }
}


export default AddUser;
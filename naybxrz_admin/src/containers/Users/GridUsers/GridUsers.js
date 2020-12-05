import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Dimmer, Icon, Label, Loader, Table} from "semantic-ui-react";
import Aux from "../../../adhoc/Aux/Aux";
import './GridUsers.css';
import * as actions from "../../../store/actions";

class GridUsers extends Component {


    checkStatusIcon(status) {
        if (status === null || status === undefined){
            return ''
        } else if (status) {
            return <Icon name='check' color='green' size='large'/>
        } else {
            return <Icon name='close' color='red' size='large'/>
        }
    }

    checkSexIcon(sex) {
        if (sex === 'M') {
            return <Icon name='male' color='blue' size='large'/>
        } else {
            return <Icon name='female' color='pink' size='large'/>
        }
    }

    handleUser = (email) => {
        this.props.getUser(email, this.props.history);
    }

    render() {

        let errors = undefined;

        if (this.props.usersError) {
            errors = <Label color='red'>Search Error: {this.props.usersError.message}</Label>;
        }

        let getUserErrorMessage = undefined;

        if (this.props.getUserError) {
            getUserErrorMessage = <Label color='red'>Select User: {this.props.getUserError.message}</Label>;
        }

        let notfound = undefined;
        if (this.props.usersNotFound){
            notfound = <Label color='orange'>User not found !</Label>
        }

        let headers = <Table.Header>
            <Table.Row>
                <Table.HeaderCell>Email</Table.HeaderCell>
                <Table.HeaderCell>ID</Table.HeaderCell>
                <Table.HeaderCell singleLine>First name</Table.HeaderCell>
                <Table.HeaderCell singleLine>Last name</Table.HeaderCell>
                <Table.HeaderCell>Gender</Table.HeaderCell>
                <Table.HeaderCell>Phone Number</Table.HeaderCell>
                <Table.HeaderCell>Address</Table.HeaderCell>
                <Table.HeaderCell>Role</Table.HeaderCell>
                <Table.HeaderCell>Status</Table.HeaderCell>
            </Table.Row>
        </Table.Header>

        let body = undefined;
        if (this.props.usersData) {
            body = <Aux>
                <Table color='orange' size='small' collapsing compact>
                    {headers}
                    <Table.Body>
                        <Table.Row>
                            <Table.Cell selectable>
                                        <span className="search-users-email-text"
                                              onClick={() => this.handleUser(this.props.usersData.email)}>{this.props.usersData.email}</span>
                            </Table.Cell>
                            <Table.Cell>{this.props.usersData.id}</Table.Cell>
                            <Table.Cell>{this.props.usersData.firstName}</Table.Cell>
                            <Table.Cell>{this.props.usersData.lastName}</Table.Cell>
                            <Table.Cell>{this.checkSexIcon(this.props.usersData.gender)}</Table.Cell>
                            <Table.Cell>{this.props.usersData.phoneNumber}</Table.Cell>
                            <Table.Cell
                                singleLine>{this.props.usersData.address.number} {this.props.usersData.address.address} {this.props.usersData.address.city} {this.props.usersData.address.postalCode} {this.props.usersData.address.province} {this.props.usersData.address.country}</Table.Cell>
                            <Table.Cell>{this.props.usersData.role}</Table.Cell>
                            <Table.Cell>{this.checkStatusIcon(this.props.usersData.status)}</Table.Cell>
                        </Table.Row>
                    </Table.Body>
                </Table>
            </Aux>
        }

        return (
            <div className="table-search-users">
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                {errors}
                {getUserErrorMessage}
                {notfound}
                <div className="button-add-user">
                    <Button content='New User'
                            icon='plus'
                            labelPosition='left'
                            color='purple' size='mini' onClick={() => this.props.addUserClicked(this.props.history)}/>
                </div>
                {body}
            </div>
        );
    }
}


const mapStateToProps = state => {
    return {
        loading: state.user.usersLoading,
        usersData: state.user.usersData,
        usersNotFound: state.user.usersNotFound,
        usersError: state.user.usersError,
        getUserError: state.user.getUserError
    }
};

const dispatchToProps = dispatch => {
    return {
        getUser: (email, history) => dispatch(actions.getUser(email, history)),
        addUserClicked: (history) => dispatch(actions.addUserClicked(history))

    }
}

export default connect(mapStateToProps, dispatchToProps)(GridUsers);
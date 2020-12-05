import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Dimmer, Form, Label, Loader, Segment} from "semantic-ui-react";
import * as actions from './../../../store/actions/index';
import loop from './loop.png';
import './SearchUsers.css';

class SearchUsers extends Component {

    state = {
        email: '',
        error: ''
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    handleSearchUsers = () => {
        if (this.state.email !== ''){
            this.props.searchUsers(this.state.email);
            this.setState({error: ''});
        } else {
            this.setState({error: 'Email cannot be empty'});
        }
    }

    render() {

        let error = undefined;
        if (this.state.error !== ''){
            error = <Label color='red'>{this.state.error}</Label>
        }

        return (
            <div className="users-manage-div">
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <Segment inverted color='yellow' className="users-segment-div">
                    <Form size='small'>
                        <Form.Group widths='1'>
                            <Form.Input inverted
                                        size='mini'
                                        placeholder='Email..'
                                        name='email'
                                        value={this.state.email}
                                        onChange={this.handleChange}/>

                            <img alt="search" src={loop} className="users-search-loop"
                                 onClick={this.handleSearchUsers}/>
                        </Form.Group>
                        {error}
                    </Form>
                </Segment>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.user.usersLoading
    }
}

const dispatchToProps = dispatch => {
    return {
        searchUsers: (email) => dispatch(actions.searchUsers(email))
    }
}

export default connect(mapStateToProps, dispatchToProps)(SearchUsers);
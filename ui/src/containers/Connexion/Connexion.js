import React, {PureComponent} from 'react';
import {connect} from 'react-redux';
import { withRouter } from 'react-router';
import {Dimmer, Form, Label, Loader, Popup} from "semantic-ui-react";
import './Connexion.css';
import Aux from '../../hoc/Aux/Aux';
import user from './user2_yellow.png';
import userBlack from './user2_black.png';
import * as actions from "../../store/actions";

class Connexion extends PureComponent {

    state = {
        open: false,
        email: '',
        password: '',
        path: ''
    }

    componentDidMount() {
        this.setState({path: this.props.history.location.pathname});
    }

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS) {
        this.setState({path: this.props.history.location.pathname});
    }

    show = (size) => () => this.setState({size, open: true});
    close = () => this.setState({open: false});
    handleChange = (e, {name, value}) => this.setState({[name]: value});

    login = () => {
        // false = redirect to userAccount
        this.props.loginUser(this.state.email, this.state.password, this.props.history, false);
        this.props.fetchOrdersHistory(this.state.email);

    };

    checkIfAuthenticatedAndRedirect = () => {
        if (this.props.userAuthenticated !== ''){
            this.props.fetchOrdersHistory(this.props.userAuthenticated.email);
            this.props.history.push('/userAccount');
        }
    }


    render() {

        let userIcon = <img src={user}
                            alt="naybxrz econcept store vintage clothes 90's adidas carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle hoodies shirts hat bob"
                            onClick={this.checkIfAuthenticatedAndRedirect}
                            className="Connexion-Button"/>
        if (this.state.path === '/men'
            || this.state.path === '/women'
            || this.state.path.startsWith('/products/')
            || this.state.path === '/checkout'
            || this.state.path === '/orders') {

            userIcon = <img src={userBlack}
                            alt="connexion"
                            onClick={this.checkIfAuthenticatedAndRedirect}
                            className="Connexion-Button"/>
        }

        let errorLabel = undefined;
        if (this.props.loginFailError !== undefined){
            errorLabel = <Label color="red">Oops! {this.props.loginFailError}</Label>
        }

        return (
            <Aux>
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <div>

                    <Popup pinned on='click'
                           position="bottom center"
                           trigger={userIcon}>

                    <Popup.Content>
                        <Form className="Connexion-div">
                            <Form.Field inline>
                                <p className="Connexion-Email-Text">EMAIL:</p>
                                <Form.Input size="small"
                                            inverted
                                            name='email'
                                            placeholder='Email'
                                            className="Connexion-Email-Text"
                                            value={this.state.email}
                                            onChange={this.handleChange}/>
                            </Form.Field>
                            <Form.Field inline>
                                <p className="Connexion-Email-Text">PASSWORD:</p>
                                <Form.Input size="small"
                                            inverted
                                            name='password'
                                            placeholder='Password'
                                            type='password'
                                            className="Connexion-Email-Text"
                                            value={this.state.password}
                                            onChange={this.handleChange} />
                                {errorLabel}
                            </Form.Field>
                            <button className="Connexion-Continue-Button" onClick={this.login}>
                                <span className="Connexion-Text-Button">SIGN IN</span>
                            </button>
                        </Form>
                    </Popup.Content>
                    </Popup>
                </div>
            </Aux>
        );
    }
}

const mapStateToProps = state => {
    return {
        userAuth: state.users.userAuth,
        userAuthenticated: state.users.userAuthenticated,
        status: state.users.status,
        loading: state.users.loading,
        error: state.users.error,
        loginFailError: state.users.loginFailError
    }
};

const dispatchProps = dispatch => {
    return {
        loginUser: (email, password, history, order) => dispatch(actions.loginUser(email, password, history, order)),
        fetchOrdersHistory: (email) => dispatch(actions.fetchOrdersHistory(email))
    }
}
const ConnexionWithRouter = withRouter(Connexion);
export default connect(mapStateToProps, dispatchProps)(ConnexionWithRouter);
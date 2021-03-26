import React, {Component} from 'react';
import {Dimmer, Form, Grid, Input, Label, Loader, Message, Modal, Segment} from "semantic-ui-react";
import {connect} from 'react-redux';
import Banner from "../../components/Banner/Banner";
import './Checkout.css';
import Contact from "../Contact/Contact";
import * as actions from "../../store/actions";
import uuid from "uuid/v1";


class Checkout extends Component {

    state = {
        email: '',
        password: '',
        open: false,
        firstName: '',
        lastName: '',
        newEmail: '',
        newPassword: '',
        num: '',
        avenue: '',
        city: '',
        postalCode: '',
        country: '',
        disabled: false,
        openForgotPassword: false,
        forgotPasswordEmail: '',
        forgotPasswordValidation: undefined
    }

/*    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS): void {
        if (prevProps.userAuth.email !== this.props.userAuth.email
            && prevProps.userAuth.password !== this.props.userAuth.password) {
            this.setState({
                email: this.props.userAuth.email,
                password: this.props.userAuth.password
            })
        }
    }*/

    show = () => this.setState({open: true});
    showForgotPassword = () => this.setState({openForgotPassword: true});
    close = () => this.setState({open: false});
    closeForgotPassword = () => {
        this.setState({openForgotPassword: false,
            forgotPasswordValidation: undefined,
            forgotPasswordEmail: ''
        });
        this.props.handleForgotPasswordError(undefined);
        this.props.handleForgotPasswordSuccess(undefined);
    }
    handleChange = (e, {name, value}) => this.setState({[name]: value});


    continueAsGuest = () => {
        this.props.history.push('/orders');
    };

    login = () => {
        this.props.loginUser(this.state.email, this.state.password, this.props.history, true);
    };

    forgotPasswordAction = () => {
        if (this.state.forgotPasswordEmail !== ''){
            this.props.forgotPassword(this.state.forgotPasswordEmail);
            this.setState({forgotPasswordValidation: undefined});
        } else {
            this.setState({forgotPasswordValidation: 'We need your email ... you know..'})
        }
    }

    addUser = () => {
        if (this.state.firstName !== ''
            && this.state.lastName !== ''
            && this.state.newEmail !== ''
            && this.state.newPassword !== ''
            && this.state.city !== ''
            && this.state.num !== ''
            && this.state.avenue !== ''
            && this.state.postalCode !== ''
            && this.state.country !== '') {
            this.props.addUser(this.constructUser(), this.props.history, true);
            this.setState({
                email: this.props.userAuth.email,
                password: this.props.userAuth.password
            });
            this.close();
        } else {
            this.props.saveUserFail('Ooch! Please verify your info');
        }
    }

    constructUser() {
        return {
            id: uuid(),
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            email: this.state.newEmail,
            password: this.state.newPassword,
            address: {
                number: this.state.num,
                address: this.state.avenue,
                city: this.state.city,
                postalCode: this.state.postalCode,
                country: this.state.country
            }

        };
    };


    render() {
        const {open, openForgotPassword} = this.state;

        let validationError = undefined;
        if (this.state.forgotPasswordValidation !== undefined){
            validationError = <Label color="red"
                                     className="Checkout-Error-Text">{this.state.forgotPasswordValidation}</Label>
        }

        let forgotPasswordCallError = undefined;
        if (this.props.forgotPasswordSuccess === 'not found'){
            forgotPasswordCallError = <Label color="red"
                                             className="Checkout-Error-Text">Sorry ! Cannot find your email</Label>
        } else if (this.props.forgotPasswordSuccess !== undefined
            && this.props.forgotPasswordSuccess.email !== undefined){
            forgotPasswordCallError = <Label color="green"
                                           className="Checkout-Error-Text">Alright! Check your email to reset the password.</Label>
        }

        let forgotPasswordServerError = undefined;
        if (this.props.forgotPasswordError !== undefined
            && this.props.forgotPasswordError.message !== undefined){
            forgotPasswordServerError = <Label color="red"
                                               className="Checkout-Error-Text">Sorry! Cannot process your request.. Please try later.</Label>
        }


        return (
            <div>
                <div className="Checkout-Yellow-bar-div"/>
                <div>
                    <header>
                        <Banner {...this.props}/>
                    </header>
                </div>
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <Grid centered className="Checkout-Grid">
                    <Grid.Row>
                        <Grid.Column mobile={20}
                                     computer={6}
                                     tablet={8}
                                     floated='left'>
                            <p className="Checkout-Message-Text">Choose what you prefer:</p>
                        </Grid.Column>

                        <Grid.Column mobile={20}
                                     computer={6}
                                     tablet={8}
                                     className="Div-As-Member-Title-Line">
                            <p className="Checkout-Message-Text">Sign As Naybxrz Player</p>
                        </Grid.Column>
                    </Grid.Row>

                    <Grid.Row>
                        <Grid.Column width={4}>
                            <Grid className="Checkout-Inner-Grid">
                                <Grid.Row className="Div-Choose-Line">
                                    <button className="Checkout-Continue-Button" onClick={this.show}>
                                        <span className="Checkout-Text-Button">New Naybxrz player ?</span>
                                    </button>
                                </Grid.Row>
                                <Grid.Row className="Div-Choose-Line">
                                    <Message color="black"
                                             size='mini'
                                             className="New-Member-Note-Message-Label" >
                                        You want to join Naybxrz Familly? <br/>
                                        Register and make your shopping on Naybxrz easier. <br/>
                                        You will be informed in advance of our special offers and arrivals of new items.
                                    </Message>
                                </Grid.Row>
                                <Grid.Row className="Div-Choose-Or-Line">
                                    <Grid.Column mobile={4}
                                                 computer={7}
                                                 tablet={8}>
                                        <div className="Choose-Empty-Left-Div"/>
                                    </Grid.Column>
                                    <span className="Checkout-Divider-Choose">OR</span>
                                    <Grid.Column mobile={4}
                                                 computer={7}
                                                 tablet={8}>
                                        <div className="Choose-Empty-Right-Div"/>
                                    </Grid.Column>
                                </Grid.Row>
                                <Grid.Row className="Div-Choose-Line">
                                    <button className="Checkout-Continue-Button" onClick={this.continueAsGuest}>
                                        <span className="Checkout-Text-Button">Continue as guest</span>
                                    </button>
                                </Grid.Row>
                            </Grid>
                        </Grid.Column>
                        <Grid.Column mobile={3}
                                     computer={3}
                                     tablet={3}>
                            <div className="Vertical-Empty-Div"/>
                        </Grid.Column>
                        <Grid.Column width={6} className="Div-As-Member-Line">
                            <Grid>
                                <Grid.Row>
                                    <Grid.Column width={5}>
                                        <span className="Checkout-Email-Text">EMAIL:</span>
                                    </Grid.Column>
                                    <Grid.Column width={5} >
                                        <Input inverted
                                               name='email'
                                               placeholder='Email'
                                               className="Checkout-Email-Text-Input"
                                               value={this.state.email}
                                               onChange={this.handleChange}

                                        />
                                    </Grid.Column>
                                </Grid.Row>

                                <Grid.Row>
                                    <Grid.Column width={5}>
                                        <span className="Checkout-Email-Text">PASSWORD:</span>
                                    </Grid.Column>

                                    <Grid.Column width={5}>
                                        <Input inverted
                                               name='password'
                                               placeholder='Password'
                                               type='password'
                                               className="Checkout-Email-Text-Input"
                                               value={this.state.password}
                                               onChange={this.handleChange}
                                        />
                                    </Grid.Column>
                                </Grid.Row>
                                <Grid.Row centered>
                                    <span className="Checkout-forgot-password-text"
                                          style={{fontStyle: "italic",
                                              textDecoration: "underline"}}
                                          onClick={this.showForgotPassword}>Forgot Password?</span>
                                </Grid.Row>
                                <Grid.Row centered>
                                    {this.props.loginFailError !== undefined &&
                                    <Label color="red" className="Checkout-Error-Text">{this.props.loginFailError}</Label>}
                                    {this.props.saveError !== undefined &&
                                    <Label color="red" className="Checkout-Error-Text">Cannot save user - please try later..</Label>}
                                    {this.props.addedUser &&
                                    <Label color="green" className="Checkout-Error-Text">Welcome to Naybxrz Familly !</Label>}
                                </Grid.Row>
                                <Grid.Row centered>
                                    <button className="Checkout-Continue-Button" onClick={this.login}>
                                        <span className="Checkout-Text-Button">SIGN IN</span>
                                    </button>
                                </Grid.Row>
                            </Grid>
                        </Grid.Column>
                    </Grid.Row>
                    <Grid.Row centered>
                        <div className="Checkout-Empty-Div"/>
                    </Grid.Row>
                    <Grid.Row>
                        <div className="Checkout-footer">
                            <Contact/>
                        </div>
                    </Grid.Row>
                </Grid>


                <Modal open={open} onClose={this.close}
                       className="Modal-Div"
                       closeIcon>
                    <Modal.Header>
                        <span className="Checkout-Message-Text">Enter you personal info:</span>
                        <br/>
                        <span className="Checkout-Mandatory-Info-Text">All the fields are mandatory*</span>
                        {this.props.saveError !== undefined &&
                        <Label color="red"
                               className="Checkout-Error-Text">{this.props.saveError}</Label>}
                    </Modal.Header>
                    <Modal.Content>
                        <Grid centered>
                            <Grid.Row>
                                <Grid.Column width={6}>
                                    <Grid.Row>
                                        <Grid.Column width={3}>
                                            <Form.Input inverted
                                                        required
                                                        placeholder='First Name...'
                                                        className="Info-Text"
                                                        name='firstName'
                                                        value={this.state.firstName}
                                                        onChange={this.handleChange}/>
                                        </Grid.Column>
                                    </Grid.Row>
                                    <Grid.Row>
                                        <Grid.Column width={3}>
                                            <Form.Input inverted
                                                        placeholder='Last Name...'
                                                        className="Info-Text"
                                                        name='lastName'
                                                        value={this.state.lastName}
                                                        onChange={this.handleChange}/>
                                        </Grid.Column>
                                    </Grid.Row>

                                    <Grid.Row>
                                        <Grid.Column width={4}>
                                            <Form.Input inverted
                                                        placeholder='Email Address...'
                                                        className="Info-Text"
                                                        name='newEmail'
                                                        value={this.state.newEmail}
                                                        onChange={this.handleChange}/>
                                        </Grid.Column>
                                    </Grid.Row>

                                    <Grid.Row>
                                        <Grid.Column width={3}>
                                            <Form.Input inverted
                                                        placeholder='Password'
                                                        name='newPassword'
                                                        type='password'
                                                        className="Info-Text"
                                                        value={this.state.newPassword}
                                                        onChange={this.handleChange}/>
                                        </Grid.Column>
                                    </Grid.Row>
                                </Grid.Column>

                                <Grid.Column>
                                    <div className="Account-Empty-Div"/>
                                </Grid.Column>

                                <Grid.Column width={5}>
                                    <Grid.Row>
                                        <Grid.Column width={3}>
                                            <Form.Input inverted placeholder='N°'
                                                        name='num'
                                                        className="Info-Text"
                                                        value={this.state.num}
                                                        onChange={this.handleChange}/>
                                            <Form.Input inverted
                                                        placeholder='Street/Avenue'
                                                        name='avenue'
                                                        className="Info-Text"
                                                        value={this.state.avenue}
                                                        onChange={this.handleChange}/>
                                            <Form.Input inverted placeholder='City'
                                                        name='city'
                                                        className="Info-Text"
                                                        value={this.state.city}
                                                        onChange={this.handleChange}/>
                                            <Form.Input inverted placeholder='Postal Code'
                                                        name='postalCode'
                                                        className="Info-Text"
                                                        value={this.state.postalCode}
                                                        onChange={this.handleChange}/>
                                            <Form.Input inverted placeholder='Country'
                                                        name='country'
                                                        className="Info-Text"
                                                        value={this.state.country}
                                                        onChange={this.handleChange}/>
                                        </Grid.Column>
                                    </Grid.Row>
                                </Grid.Column>
                            </Grid.Row>
                        </Grid>

                    </Modal.Content>
                    <Modal.Actions>
                        <button className="Account-Button" onClick={this.addUser}>
                            <span className="Account-Text-Button">JOIN THE CREW</span>
                        </button>
                    </Modal.Actions>

                </Modal>

                <Modal open={openForgotPassword}
                       onClose={this.closeForgotPassword}
                       className="Modal-Div"
                       closeIcon>
                    <Dimmer active={this.props.loading} page>
                        <Loader content='Loading'/>
                    </Dimmer>
                    <Modal.Header>
                        <span className="Checkout-Message-Text">Please enter your email:</span>
                    </Modal.Header>
                    <Modal.Content>
                        <Grid centered>
                            <Grid.Row>
                                <Grid.Column width={5}>
                                    <span className="Checkout-Email-Text">EMAIL:</span>
                                </Grid.Column>
                                <Grid.Column width={5} >
                                    <Input
                                           name='forgotPasswordEmail'
                                           placeholder='Email'
                                           className="Checkout-Email-Text-Input"
                                           value={this.state.forgotPasswordEmail}
                                           onChange={this.handleChange} />
                                </Grid.Column>
                            </Grid.Row>
                            <Grid.Row>
                                {validationError}
                                {forgotPasswordCallError}
                                {forgotPasswordServerError}
                            </Grid.Row>
                        </Grid>
                    </Modal.Content>
                    <Modal.Actions>
                        <button className="Account-Button" onClick={this.forgotPasswordAction}>
                            <span className="Account-Text-Button">Reset Password</span>
                        </button>
                    </Modal.Actions>
                </Modal>

            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        userAuth: state.users.userAuth,
        addedUser: state.users.addedUser,
        status: state.users.status,
        loading: state.users.loading,
        loginFailError: state.users.loginFailError,
        saveError: state.users.error,
        forgotPasswordLoading: state.users.forgotPasswordLoading,
        forgotPasswordSuccess: state.users.forgotPasswordSuccess,
        forgotPasswordError: state.users.forgotPasswordError
    }
};

const dispatchToProps = dispatch => {
    return {
        loginUser: (email, password, history, order) => dispatch(actions.loginUser(email, password, history, order)),
        addUser: (userToAdd, history, isNew) => dispatch(actions.saveUser(userToAdd, history, isNew)),
        saveUserFail: (message) => dispatch(actions.saveUserFail(message)),
        forgotPassword: (email) => dispatch(actions.forgotPassword(email)),
        handleForgotPasswordError: (error) => dispatch(actions.handleForgotPasswordError(error)),
        handleForgotPasswordSuccess: (user) => dispatch(actions.handleForgotPasswordSuccess(user))
    }
};

export default connect(mapStateToProps, dispatchToProps)(Checkout);
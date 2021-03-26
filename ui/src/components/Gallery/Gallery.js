import React, {Component} from 'react';
import {connect} from 'react-redux';
import * as actions from './../../store/actions/index';
import './Gallery.css';

class Gallery extends Component {

    redirectToProduct = (id) => {
        this.props.getProductById(id, this.props.history);
    }

    render() {
        return (
            <div>
                <img alt="" className="Gallery-Div" src={this.props.url} onClick={() => this.redirectToProduct(this.props.productId)}/>
            </div>
        );
    }
}

const dispatchToProps = dispatch => {
    return {
        getProductById: (productId, history) => dispatch(actions.loadProduct(productId, history))
    }
}

export default connect(null, dispatchToProps)(Gallery);
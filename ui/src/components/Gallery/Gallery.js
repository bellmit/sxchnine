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
                <img alt="naybxrz econcept store vintage clothes 90's adidas carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle hoodies shirts hat bob" className="Gallery-Div" src={this.props.url} onClick={() => this.redirectToProduct(this.props.productId)}/>
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
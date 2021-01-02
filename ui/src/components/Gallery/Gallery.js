import React, {Component} from 'react';
import './Gallery.css';

class Gallery extends Component {

    redirectToProduct = (id) => {
        this.props.history.push('/products/'+id);
    }

    render() {
        return (
            <div>
                <img alt="" className="Gallery-Div" src={this.props.url} onClick={() => this.redirectToProduct(this.props.productId)}/>
            </div>
        );
    }


}

export default Gallery;
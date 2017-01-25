package itspay.br.com.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.view.MaterialListView;

import java.util.ArrayList;
import java.util.List;

import itspay.br.com.controller.LojaProdutosController;
import itspay.br.com.itspay.R;
import itspay.br.com.model.ParceiroResponse;
import itspay.br.com.model.Produto;
import itspay.br.com.util.Utils;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LojaProdutosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LojaProdutosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LojaProdutosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    public SwipeRefreshLayout swipeRefreshLayout;

    public MaterialListView materialListView;

    public ParceiroResponse listaParceiroResponse[];

    private LojaProdutosController controller = new LojaProdutosController();

    public LojaProdutosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LojaProdutosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LojaProdutosFragment newInstance(String param1, String param2) {
        LojaProdutosFragment fragment = new LojaProdutosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_loja_produtos, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
//
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                controller.listaParceiros(LojaProdutosFragment.this);
            }
        });
        materialListView = (MaterialListView) rootView.findViewById(R.id.material_listview);

        controller.listaParceiros(LojaProdutosFragment.this);

        return rootView;
    }

    public void listarProdutos(){
        materialListView.setItemAnimator(new FlipInBottomXAnimator());
        materialListView.getItemAnimator().setAddDuration(300);
        materialListView.getItemAnimator().setRemoveDuration(300);

        materialListView.getAdapter().clearAll();

        List<Card> cards = new ArrayList<>();

        for(ParceiroResponse parceiroResponse: listaParceiroResponse) {
            for (Produto produto : parceiroResponse.getProdutos()) {

                String precoDe = "R$" + Utils.formataMoeda(produto.getReferencias()[0].getPrecoDe());
                String precoPor = "R$" + Utils.formataMoeda(produto.getReferencias()[0].getPrecoPor());
                String description = parceiroResponse.getQuantMaxParcelaSemJuros() +
                        "x de R$" +
                        Utils.formataMoeda(
                                produto.getReferencias()[0].getPrecoPor() /
                                        parceiroResponse.getQuantMaxParcelaSemJuros());

                Card card = new Card.Builder(this.getContext())
                        .setTag("Produto Pedido")
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.item_produto_loja)
                        .setTitle(produto.getNomeProduto())
                        .setTitleColor(Color.DKGRAY)
                        .setSubtitle(precoDe)
                        .setSubtitle2(precoPor)
                        .setSubtitle3(description)
                        .setSubtitleColor(Color.BLACK)
                        .endConfig()
                        .build();

                cards.add(card);
            }

        }
        materialListView.getAdapter().addAll(cards);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
